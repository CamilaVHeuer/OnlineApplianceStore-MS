package com.camicompany.products_service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProductDTO(
        @Schema(description = "Code of the Product.", example = "A1")
                               @NotBlank(message = "Product code is required")
                               String code,
                               @Schema(example = "Refrigerator")
                               @NotBlank (message = "Product name is required")
                               String name,
                               @Schema(example="Samsung")
                               @NotBlank (message = "Product brand is required")
                               String brand,
                               @Schema(example = "1000.00")
                               @NotNull(message = "Unit price is required")
                               @PositiveOrZero(message = "Unit price must be zero or positive")
                               Double unitPrice,
                               @Schema(example = "50")
                               @NotNull(message = "Stock is required")
                               @PositiveOrZero(message = "Stock must be zero or positive")
                               Integer stock){
}
