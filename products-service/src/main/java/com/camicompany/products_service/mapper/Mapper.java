package com.camicompany.products_service.mapper;

import com.camicompany.products_service.dto.ProductDTO;
import com.camicompany.products_service.model.Product;

public class Mapper {
    // This class can be used for mapping between entities and DTOs
    //convert Product to ProductDTO
    public static ProductDTO toDTO(Product p) {
        if (p == null) {
            return null;
        }
        return ProductDTO.builder().
                productId(p.getProductId()).
                code(p.getCode()).
                name(p.getName()).
                brand(p.getBrand()).
                unitPrice(p.getUnitPrice()).
                stock(p.getStock()).
                status(p.getStatus()).
                build();
    }
}
