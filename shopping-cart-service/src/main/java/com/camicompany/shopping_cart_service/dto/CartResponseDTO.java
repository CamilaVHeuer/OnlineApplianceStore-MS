package com.camicompany.shopping_cart_service.dto;

import com.camicompany.shopping_cart_service.model.CartStatus;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.List;


public record CartResponseDTO(
    @Schema(description = "Unique identifier of the cart", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    @Schema(description = "Username of the user who owns this cart", example ="user1", accessMode = Schema.AccessMode.READ_ONLY)
    String username,
    @Schema( example = "1500.00", accessMode = Schema.AccessMode.READ_ONLY)
    Double totalPrice,
    @Schema(description = "List of items in the cart")
    List<CartItemResponseDTO> items,
    @Schema(description = "Status of the cart", example = "CREATED", accessMode = Schema.AccessMode.READ_ONLY)
    CartStatus status) {}
