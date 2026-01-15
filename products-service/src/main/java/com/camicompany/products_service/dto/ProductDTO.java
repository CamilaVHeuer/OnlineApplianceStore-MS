package com.camicompany.products_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @Schema(description = "Unique identifier of the Product.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long productId;
    @Schema(description = "Code of the Product.", example = "A1")
    private String code;
    @Schema(example = "Refrigerator")
    private String name;
    @Schema(example="Samsung")
    private String brand;
    @Schema(example = "1000.00")
    private Double unitPrice;
    @Schema(example = "50")
    private Integer stock;
}
