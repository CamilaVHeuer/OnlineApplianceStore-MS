package com.camicompany.products_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateProductDTO(
                               @Schema(description = "Code of the Product.", example = "A1")
                               String code,
                               @Schema(example = "Refrigerator")
                               String name,
                               @Schema(example="Samsung")
                               String brand,
                               @Schema(example = "1000.00")
                               @PositiveOrZero(message = "Stock must be zero or positive")
                               Double unitPrice,
                               @Schema(example = "50")
                               @PositiveOrZero(message = "Stock must be zero or positive")
                               Integer stock) {
}
