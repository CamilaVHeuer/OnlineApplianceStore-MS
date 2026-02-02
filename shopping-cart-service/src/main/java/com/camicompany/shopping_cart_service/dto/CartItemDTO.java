package com.camicompany.shopping_cart_service.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record CartItemDTO (
    @NotNull(message = "Product ID is required")
    Long productId,
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than zero")
    Integer quantity){

}
