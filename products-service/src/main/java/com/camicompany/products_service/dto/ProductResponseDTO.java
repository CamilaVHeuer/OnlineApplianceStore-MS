package com.camicompany.products_service.dto;

import com.camicompany.products_service.model.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;



public record ProductResponseDTO(
    @Schema(example = "1")
    Long productId,
    @Schema(example = "A1")
     String code,
    @Schema(example = "Refrigerator")
    String name,
    @Schema(example="Samsung")
    String brand,
    @Schema(example = "1000.00")
    Double unitPrice,
    @Schema(example = "50")
    Integer stock,
    @Schema(example = "ACTIVE")
    ProductStatus status)
{}
