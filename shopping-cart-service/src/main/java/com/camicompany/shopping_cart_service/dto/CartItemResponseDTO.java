package com.camicompany.shopping_cart_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;


public record CartItemResponseDTO(@Schema(description = "Unique identifier of the cart item", example = "1")
                                  Long itemId,
                                  @Schema(description = "Unique identifier of the product", example = "1")
                                  Long productId,
                                  @Schema(description = "Quantity of the product in the cart", example = "2")
                                  Integer quantity) {
}
