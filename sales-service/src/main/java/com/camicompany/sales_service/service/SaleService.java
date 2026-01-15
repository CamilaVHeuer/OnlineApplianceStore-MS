package com.camicompany.sales_service.service;

import com.camicompany.sales_service.dto.CartDTO;
import com.camicompany.sales_service.dto.CartItemDTO;
import com.camicompany.sales_service.dto.SaleDTO;
import com.camicompany.sales_service.dto.SaleDateDTO;
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
    public List<SaleDTO> getAllSales() {
        return saleRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public SaleDTO getSaleById(Long saleId) {
        Sale sale = saleRepo.findById(saleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale not found"));
        return Mapper.toDTO(sale);
    }


    @Override
    public List<SaleDTO> getSalesByDate(LocalDate date) {
        return saleRepo.findByDate(date).stream().map(Mapper::toDTO).toList();
    }

    @Override
    public SaleDTO createSale(SaleDTO saleDTO) {
        if (saleDTO == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale data is required");}
        if (saleDTO.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale date is required");
        }
        if (saleDTO.getDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale date cannot be in the future");
        }
        if (saleDTO.getCartId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CartId is required");}

        Sale sale = new Sale();
        CartDTO car = self.getCartSafe(saleDTO.getCartId());

        for (CartItemDTO item : car.getItems()) {
            // Check stock and decrease
            self.decreaseStockSafe(item.getProductId(), item.getQuantity());
        }

        sale.setDate(saleDTO.getDate());
        sale.setCartId(car.getId());
        sale.setTotalAmount(car.getTotalPrice());
        sale.setStatus(SaleStatus.CREATED);
        return Mapper.toDTO(saleRepo.save(sale));
    }

    @Retry(name = "products-service")
    @CircuitBreaker(name = "products-service", fallbackMethod = "fallbackdecreaseStock")
    public void decreaseStockSafe(Long productId, Integer quantity) {
        prodAPI.decreaseStock(productId, quantity);
    }

    @Retry(name = "shopping-cart-service")
    @CircuitBreaker(name = "shopping-cart-service", fallbackMethod = "fallbackgetCart")
    public CartDTO getCartSafe(Long cartId) {
        return cartAPI.getCartById(cartId);
    }

    public void fallbackdecreaseStock(Long productId, Integer quantity, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Product service is unavailable. Please try again later.");
    }

    public CartDTO fallbackgetCart(Long cartId, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Shopping Cart service is unavailable. Please try again later.");
    }

    @Override
    public SaleDTO updateSale(Long saleId, SaleDateDTO saleDateDTO) {
        if (saleDateDTO == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale data is required");}
        Sale existingSale = saleRepo.findById(saleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale not found"));
        if (saleDateDTO.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale date is required");
        }
        if (saleDateDTO.getDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale date cannot be in the future");
        }
        existingSale.setDate(saleDateDTO.getDate());
        return Mapper.toDTO(saleRepo.save(existingSale));

    }

    @Transactional
    @Override
    public SaleDTO cancelSale(Long saleId) {
        Sale sale = saleRepo.findById(saleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale not found"));
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
        for (CartItemDTO item : car.getItems()) {
            self.restoreStockSafe(item.getProductId(), item.getQuantity());
        }
        sale.setStatus(SaleStatus.STOCK_RESTORED);
        saleRepo.save(sale);

        sale.setStatus(SaleStatus.CANCELLED);
        return Mapper.toDTO(saleRepo.save(sale));
    }

    @Retry(name = "products-service")
    @CircuitBreaker(name = "products-service", fallbackMethod = "fallbackrestoreStock")
    public void restoreStockSafe(Long productId, Integer quantity) {
        prodAPI.restoreStock(productId, quantity);
    }

    public void fallbackrestoreStock(Long productId, Integer quantity, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Unable to cancel sale because product service is unavailable");
    }

}
