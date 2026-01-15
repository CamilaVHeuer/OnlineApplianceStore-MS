package com.camicompany.shopping_cart_service.service;

import com.camicompany.shopping_cart_service.dto.CartDTO;
import com.camicompany.shopping_cart_service.dto.CartItemDTO;
import com.camicompany.shopping_cart_service.dto.ProductDTO;
import com.camicompany.shopping_cart_service.mapper.Mapper;
import com.camicompany.shopping_cart_service.model.Cart;

import com.camicompany.shopping_cart_service.model.CartItem;
import com.camicompany.shopping_cart_service.model.CartStatus;
import com.camicompany.shopping_cart_service.repository.ICartRepository;
import com.camicompany.shopping_cart_service.repository.IProductAPI;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepo;

    @Autowired
    private IProductAPI productAPI;

    @Autowired
    @Lazy
    private CartService self;

    @Override
    public List<CartDTO> getAllCarts() {
        return cartRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public CartDTO getCartById(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        return Mapper.toDTO(cart);
    }

    @Override
    public void deleteCart(Long cartId) {
        Cart cart= cartRepo.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        if (cart.getStatus() == CartStatus.SOLD) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete a sold cart");
        }
        cartRepo.deleteById(cartId);

    }

    @Override
    public CartDTO createCart(CartDTO cartDTO) {
        if (cartDTO == null || cartDTO.getItems() == null || cartDTO.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart items are required");
        }
        Cart cart = new Cart();
        List<CartItem> items = new ArrayList<>();

        for (CartItemDTO itemDTO : cartDTO.getItems()) {
            if (itemDTO.getProductId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product ID is required for each item");
            }
            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Quantity must be greater than zero for each item");
            }

            ProductDTO product = self.getProductById(itemDTO.getProductId());

            if (product.getStock() < itemDTO.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Insufficient stock for product ID: " + itemDTO.getProductId());
            }

            CartItem item = new CartItem();
            item.setProductId(product.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(product.getUnitPrice());
            item.setCart(cart);

            items.add(item);
        }
        cart.setItems(items);
        cart.setStatus(CartStatus.CREATED);

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getUnitPrice() * i.getQuantity())
                .sum();
        cart.setTotalPrice(total);

        Cart savedCart = cartRepo.save(cart);
        return Mapper.toDTO(savedCart);

    }

    @CircuitBreaker(name = "products-service", fallbackMethod = "fallbackGetProductById")
    @Retry(name = "products-service")
    public ProductDTO getProductById(Long productId) {
        return  productAPI.getProductById(productId);
    }

    public ProductDTO fallbackGetProductById(Long productId, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Product service is unavailable. Please try again later.");
    }

    @Override
    public CartDTO updateCart(Long cartId, CartDTO cartDTO) {
        if (cartDTO == null || cartDTO.getItems() == null || cartDTO.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart items are required");
        }
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        if (cart.getStatus() == CartStatus.SOLD) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot update a sold cart");
        }

        List<CartItem> updatedItems = new ArrayList<>();

        for (CartItemDTO itemDTO : cartDTO.getItems()) {
            if (itemDTO.getProductId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product ID is required");
            }

            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
            }

            ProductDTO product = self.getProductById(itemDTO.getProductId());

            if (product.getStock() < itemDTO.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Insufficient stock for product ID: " + itemDTO.getProductId());
            }
            CartItem cartItem = new CartItem();

            cartItem.setProductId(product.getProductId());
            cartItem.setUnitPrice(product.getUnitPrice());
            cartItem.setQuantity(itemDTO.getQuantity());

            updatedItems.add(cartItem);
        }

        cart.getItems().clear();

        for (CartItem item : updatedItems) {
            item.setCart(cart);
            cart.getItems().add(item);
        }

        double total = updatedItems.stream()
                .mapToDouble(i -> i.getUnitPrice() * i.getQuantity())
                .sum();

        cart.setTotalPrice(total);

        Cart savedCart = cartRepo.save(cart);
        return Mapper.toDTO(savedCart);
    }

    @Override
    public void markCartAsSold(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        if (cart.getStatus() == CartStatus.SOLD) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cart is already sold");
        }

        cart.setStatus(CartStatus.SOLD);
        cartRepo.save(cart);
    }

}
