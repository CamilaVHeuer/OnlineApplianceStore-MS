package com.camicompany.products_service.mapper;

import com.camicompany.products_service.dto.ProductResponseDTO;
import com.camicompany.products_service.model.Product;

public class Mapper {
    // This class can be used for mapping between entities and DTOs
    //convert Product to ProductDTO
    public static ProductResponseDTO toDTO(Product p) {
        if (p == null) {
            return null;
        }
        return new ProductResponseDTO(
                p.getProductId(),
                p.getCode(),
                p.getName(),
                p.getBrand(),
                p.getUnitPrice(),
                p.getStock(),
                p.getStatus());
    }
}
