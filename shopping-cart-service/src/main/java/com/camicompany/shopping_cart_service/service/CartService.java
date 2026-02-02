package com.camicompany.shopping_cart_service.service;

import com.camicompany.shopping_cart_service.dto.*;
import com.camicompany.shopping_cart_service.mapper.Mapper;
import com.camicompany.shopping_cart_service.model.Cart;

import com.camicompany.shopping_cart_service.model.CartItem;
import com.camicompany.shopping_cart_service.model.CartStatus;
import com.camicompany.shopping_cart_service.repository.ICartRepository;
import com.camicompany.shopping_cart_service.repository.IProductAPI;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService implements ICartService {

    public static final String PRODUCT_STATUS_ACTIVE = "ACTIVE";

    @Autowired
    private ICartRepository cartRepo;

    @Autowired
    private IProductAPI productAPI;

    @Autowired
    @Lazy
    private CartService self;

    @Override
    public List<CartResponseDTO> getAllCarts() {
        return cartRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public CartResponseDTO getCartById(Long cartId) {
        Cart cart = findCartOrThrow(cartId);
        return Mapper.toDTO(cart);
    }

    @Override
    public void deleteCart(Long cartId) {
        Cart cart = findCartOrThrow(cartId);
        ensureCartIsModifiable(cart);
        cartRepo.deleteById(cartId);

    }

    @Override
    public CartResponseDTO createCart(CreateCartDTO createCartDTO) {

        Cart cart = new Cart();
        List<CartItem> items = new ArrayList<>();

        for (CartItemDTO itemDTO : createCartDTO.items()) {

            ProductDTO product = self.getProductById(itemDTO.productId());
            if(!PRODUCT_STATUS_ACTIVE.equals(product.status())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "product is not active and cannot be added to cart. Product ID: " + itemDTO.productId());
            }

            if (product.stock() < itemDTO.quantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Insufficient stock for product ID: " + itemDTO.productId());
            }

            CartItem item = new CartItem();
            item.setProductId(product.productId());
            item.setQuantity(itemDTO.quantity());
            item.setUnitPrice(product.unitPrice());
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
    public CartResponseDTO updateCart(Long cartId, UpdateCartDTO updateCartDTO) {

        Cart cart = findCartOrThrow(cartId);

        ensureCartIsModifiable(cart);

        List<CartItem> updatedItems = new ArrayList<>();

        for (CartItemDTO itemDTO : updateCartDTO.items()) {

            ProductDTO product = self.getProductById(itemDTO.productId());
            if(!PRODUCT_STATUS_ACTIVE.equals(product.status())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "product is not active and cannot be added to cart. Product ID: " + itemDTO.productId());
            }

            if (product.stock() < itemDTO.quantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Insufficient stock for product ID: " + itemDTO.productId());
            }

            CartItem cartItem = new CartItem();

            cartItem.setProductId(product.productId());
            cartItem.setUnitPrice(product.unitPrice());
            cartItem.setQuantity(itemDTO.quantity());

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
        Cart cart = findCartOrThrow(cartId);

        ensureCartIsModifiable(cart);

        cart.setStatus(CartStatus.SOLD);
        cartRepo.save(cart);
    }

    //helper method
    private Cart findCartOrThrow(Long cartId) {
        return cartRepo.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
    }

    private void ensureCartIsModifiable(Cart cart) {
        if (cart.getStatus() == CartStatus.SOLD) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot modify a sold cart");
        }
    }

}
