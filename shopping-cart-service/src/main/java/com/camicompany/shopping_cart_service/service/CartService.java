package com.camicompany.shopping_cart_service.service;

import com.camicompany.shopping_cart_service.dto.CartDTO;
import com.camicompany.shopping_cart_service.dto.CartItemDTO;
import com.camicompany.shopping_cart_service.dto.ProductDTO;
import com.camicompany.shopping_cart_service.mapper.Mapper;
import com.camicompany.shopping_cart_service.model.Cart;

import com.camicompany.shopping_cart_service.model.CartItem;
import com.camicompany.shopping_cart_service.repository.ICartRepository;
import com.camicompany.shopping_cart_service.repository.IProductAPI;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService implements ICartService{

    @Autowired
    private ICartRepository cartRepo;

    @Autowired
    private IProductAPI productAPI;

    @Override
    public List<CartDTO> getAllCarts() {
        return cartRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public CartDTO getCartById(Long cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        return Mapper.toDTO(cart);
    }

    @Override
    public void deleteCart(Long cartId) {
        if (!cartRepo.existsById(cartId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
        }
        cartRepo.deleteById(cartId);

    }

    @Override
    @Retry(name="products-service")
    @CircuitBreaker(name="products-service", fallbackMethod="fallbackCreateCart")
        public CartDTO createCart(CartDTO cartDTO) {
        //lo que me importa del CartDTO son los items
        if (cartDTO == null || cartDTO.getItems() == null || cartDTO.getItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart items are required");
        }
        Cart cart= new Cart();
        List<CartItem> items = new ArrayList<>();

        //debo buscar los items en la base de datos o en el servicio de productos para obtener su precio y validar stock.
        //recorro la lista de items
        for (CartItemDTO itemDTO: cartDTO.getItems()) {
            if (itemDTO.getProductId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product ID is required for each item");
            }
            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero for each item");
            }
            //busco el producto en el servicio de productos y me lo guardo en una variable product
            ProductDTO product = productAPI.getProductById(itemDTO.getProductId());

            //valido que el stock sea suficiente
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for product ID: " + itemDTO.getProductId());
            }
            //creo los itmes del carrito (no DTO sino oficiales)
            CartItem item = new CartItem();
            item.setProductId(product.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(product.getUnitPrice());
            //seteo la relacion bidireccional
            item.setCart(cart);
            //agrego cada item a la lista de items
            items.add(item);

        }
        //agrego la lista de items al carrito
        cart.setItems(items);
        //actualizo el precio total del carrito
        double total = cart.getItems().stream()
                    .mapToDouble(i -> i.getUnitPrice() * i.getQuantity())
                    .sum();
        cart.setTotalPrice(total);

        Cart savedCart = cartRepo.save(cart);
        return Mapper.toDTO(savedCart);

    }
    public CartDTO fallbackCreateCart(CartDTO cartDTO, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Product service is unavailable. Please try again later.");
    }

    @Override
    @Retry(name="products-service")
    @CircuitBreaker(name="products-service", fallbackMethod="fallbackUpdateCart")
    public CartDTO updateCart(Long cartId, CartDTO cartDTO) {
        if(cartDTO == null || cartDTO.getItems() == null || cartDTO.getItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart items are required");
        }
        Cart cart = cartRepo.findById(cartId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        List<CartItem> updatedItems = new ArrayList<>();

        //verifico si hay cambios en los items de la request
        for (CartItemDTO itemDTO: cartDTO.getItems()) {
            if (itemDTO.getProductId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product ID is required");
            }

            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
            }

            //consulto el producto en el servicio de productos
          ProductDTO product = productAPI.getProductById(itemDTO.getProductId());

            //valido que el stock sea suficiente
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for product ID: " + itemDTO.getProductId());
            }
            //creo nuevos items del carrito
            CartItem cartItem = new CartItem();

                cartItem.setProductId(product.getProductId());
                cartItem.setUnitPrice(product.getUnitPrice());
                cartItem.setQuantity(itemDTO.getQuantity());

                updatedItems.add(cartItem);
            }


        // Reemplazar items del carrito
        cart.getItems().clear();
        //Agrego los nuevos items
        for (CartItem item: updatedItems) {
            item.setCart(cart);
            cart.getItems().add(item);
        }


        // Recalcular total
        double total = updatedItems.stream()
                .mapToDouble(i -> i.getUnitPrice() * i.getQuantity())
                .sum();

        cart.setTotalPrice(total);

        Cart savedCart = cartRepo.save(cart);
        return Mapper.toDTO(savedCart);
    }
public CartDTO fallbackUpdateCart(Long cartId, CartDTO cartDTO, Throwable t) {
    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Product service is unavailable. Please try again later.");
}
}
