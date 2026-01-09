package com.camicompany.products_service.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long productId;
    private String code;
    private String name;
    private String brand;
    private Double unitPrice;
    private Integer stock;
}
