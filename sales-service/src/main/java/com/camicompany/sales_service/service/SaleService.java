package com.camicompany.sales_service.service;

import com.camicompany.sales_service.dto.*;
import com.camicompany.sales_service.mapper.Mapper;
import com.camicompany.sales_service.model.Sale;
import com.camicompany.sales_service.model.SaleStatus;
import com.camicompany.sales_service.repository.ICartAPI;
import com.camicompany.sales_service.repository.IProductAPI;
import com.camicompany.sales_service.repository.ISaleRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleService implements ISaleService {
    public static final String CART_STATUS_CREATED = "CREATED";


    @Autowired
    private ISaleRepository saleRepo;

    @Autowired
    private ICartAPI cartAPI;

    @Autowired
    private IProductAPI prodAPI;

    @Autowired
    @Lazy
    private SaleService self;

    @Override
    public List<SaleResponseDTO> getAllSales() {
        return saleRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public SaleResponseDTO getSaleById(Long saleId) {
        Sale sale = findSaleOrThrow(saleId);
        return Mapper.toDTO(sale);
    }


    @Override
    public List<SaleResponseDTO> getSalesByDate(LocalDate date) {
        return saleRepo.findByDate(date).stream().map(Mapper::toDTO).toList();
    }

    @Override
    public SaleResponseDTO createSale(CreateSaleDTO createSaleDTO) {

        Sale sale = new Sale();
        CartDTO cart = self.getCartSafe(createSaleDTO.cartId());
        if(!CART_STATUS_CREATED.equals(cart.status())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cart is not available for sale");}

        self.markCartAsSold(cart.id());

        for (CartItemDTO item : cart.items()) {
            // Check stock and decrease
            self.decreaseStockSafe(item.productId(), item.quantity());
        }

        sale.setDate(createSaleDTO.date());
        sale.setCartId(cart.id());
        sale.setTotalAmount(cart.totalPrice());
        sale.setStatus(SaleStatus.CREATED);

        return Mapper.toDTO(saleRepo.save(sale));
    }

    @Retry(name = "products-service")
    @CircuitBreaker(name = "products-service", fallbackMethod = "fallbackDecreaseStock")
    public void decreaseStockSafe(Long productId, Integer quantity) {
        prodAPI.decreaseStock(productId, quantity);
    }

    @Retry(name = "shopping-cart-service")
    @CircuitBreaker(name = "shopping-cart-service", fallbackMethod = "fallbackGetCart")
    public CartDTO getCartSafe(Long cartId) {
        return cartAPI.getCartById(cartId);
    }

    @Retry(name = "shopping-cart-service")
    @CircuitBreaker(name = "shopping-cart-service", fallbackMethod = "fallbackMarkCartAsSold")
    public void markCartAsSold(Long cartId) {
        cartAPI.markCartAsSold(cartId);
    }

    public void fallbackDecreaseStock(Long productId, Integer quantity, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Product service is unavailable. Please try again later.");
    }

    public CartDTO fallbackGetCart(Long cartId, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Shopping Cart service is unavailable. Please try again later.");
    }

    public void fallbackMarkCartAsSold(Long cartId, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Shopping Cart service is unavailable. Please try again later.");
    }

    @Override
    public SaleResponseDTO updateSale(Long saleId, SaleDateDTO saleDateDTO) {
        Sale existingSale = findSaleOrThrow(saleId);
        existingSale.setDate(saleDateDTO.date());
        return Mapper.toDTO(saleRepo.save(existingSale));

    }

    @Transactional
    @Override
    public SaleResponseDTO cancelSale(Long saleId) {
        Sale sale = findSaleOrThrow(saleId);
        if (sale.getStatus() == SaleStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sale is already cancelled");
        }

        if (sale.getStatus() == SaleStatus.STOCK_RESTORED) {
            sale.setStatus(SaleStatus.CANCELLED);
            return Mapper.toDTO(saleRepo.save(sale));
        }

        if (sale.getStatus() != SaleStatus.CANCELLING) {
            sale.setStatus(SaleStatus.CANCELLING);
            saleRepo.save(sale);
        }

        CartDTO car = self.getCartSafe(sale.getCartId());
        for (CartItemDTO item : car.items()) {
            self.restoreStockSafe(item.productId(), item.quantity());
        }
        sale.setStatus(SaleStatus.STOCK_RESTORED);
        saleRepo.save(sale);

        sale.setStatus(SaleStatus.CANCELLED);
        return Mapper.toDTO(saleRepo.save(sale));
    }

    @Retry(name = "products-service")
    @CircuitBreaker(name = "products-service", fallbackMethod = "fallbackRestoreStock")
    public void restoreStockSafe(Long productId, Integer quantity) {
        prodAPI.restoreStock(productId, quantity);
    }

    public void fallbackRestoreStock(Long productId, Integer quantity, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Unable to cancel sale because product service is unavailable");
    }

    //Helpers methods
    private Sale findSaleOrThrow(Long saleId) {
        return saleRepo.findById(saleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sale not found"));
    }

}
