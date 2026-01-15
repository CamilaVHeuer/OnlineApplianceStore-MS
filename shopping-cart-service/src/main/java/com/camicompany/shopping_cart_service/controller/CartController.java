package com.camicompany.shopping_cart_service.controller;

import com.camicompany.shopping_cart_service.dto.CartDTO;
import com.camicompany.shopping_cart_service.service.ICartService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
@Tag(
        name = "Shopping-Cart",
        description = "Public operations for shopping cart management"
)
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    public ICartService cartServ;

    @Operation(summary = "Get all carts")
    @ApiResponse(responseCode = "200", description = "List of carts returned")
    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return ResponseEntity.ok(cartServ.getAllCarts());
    }

    @Operation(
            summary = "Get cart by ID",
            description = "Returns a cart based on its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart found"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable("id") Long cartId) {
        return ResponseEntity.ok(cartServ.getCartById(cartId));
    }

    @Operation(summary = "Create a new cart", description = "Creates a new shopping cart with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cart created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid cart data provided"),
            @ApiResponse(responseCode = "503", description = "Product service is unavailable")
    })
    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        CartDTO createdCart = cartServ.createCart(cartDTO);
        return ResponseEntity.created(URI.create("/api/cart/" + createdCart.getId())).body(createdCart);
    }

    @Operation(summary = "Delete cart by ID", description = "Deletes a cart based on its unique identifier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete a sold cart")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id")  Long cartId) {
        cartServ.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update cart by ID", description = "Updates an existing cart with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "400", description = "Invalid cart data provided"),
            @ApiResponse(responseCode = "409", description = "Cannot update a sold cart"),
            @ApiResponse(responseCode = "503", description = "Product service is unavailable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable("id") Long cartId, @RequestBody CartDTO cartDTO) {
        return ResponseEntity.ok(cartServ.updateCart(cartId, cartDTO));
    }

    @Hidden
    @PutMapping("/mark-as-sold/{id}")
    public ResponseEntity<Void> markCartAsSold(@PathVariable("id") Long cartId) {
        cartServ.markCartAsSold(cartId);
        return ResponseEntity.noContent().build();
    }

}
