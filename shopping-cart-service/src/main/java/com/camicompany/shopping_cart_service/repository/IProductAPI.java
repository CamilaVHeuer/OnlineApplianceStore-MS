package com.camicompany.shopping_cart_service.repository;

import com.camicompany.shopping_cart_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="products-service")
public interface IProductAPI {
    @GetMapping("/api/products/{id}")
    public ProductDTO getProductById(@PathVariable Long id);
}
