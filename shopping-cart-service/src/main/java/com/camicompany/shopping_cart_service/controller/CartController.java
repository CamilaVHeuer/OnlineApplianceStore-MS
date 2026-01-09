package com.camicompany.shopping_cart_service.controller;

import com.camicompany.shopping_cart_service.dto.CartDTO;
import com.camicompany.shopping_cart_service.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    public ICartService cartServ;

    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return ResponseEntity.ok(cartServ.getAllCarts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable("id") Long cartId) {
        return ResponseEntity.ok(cartServ.getCartById(cartId));
    }

    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        CartDTO createdCart = cartServ.createCart(cartDTO);
        return ResponseEntity.created(URI.create("/api/cart/" + createdCart.getId())).body(createdCart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id")  Long cartId) {
        cartServ.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable("id") Long cartId, @RequestBody CartDTO cartDTO) {
        return ResponseEntity.ok(cartServ.updateCart(cartId, cartDTO));
    }

}
