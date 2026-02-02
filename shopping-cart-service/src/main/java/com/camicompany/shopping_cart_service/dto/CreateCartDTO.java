package com.camicompany.shopping_cart_service.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateCartDTO(
                            @Schema(description = "List of items in the cart")
                            @NotNull(message = "Items list is required")
                            @NotEmpty(message = "Items list cannot be empty")
                            List<@Valid CartItemDTO> items
                            ) {
}
