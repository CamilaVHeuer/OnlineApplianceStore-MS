package com.camicompany.sales_service.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="products-service")
public interface IProductAPI {
    @PutMapping("/api/products/decrease-stock/{id}")
    public void decreaseStock(@PathVariable Long id, @RequestBody Integer quantity);

    @PutMapping("/api/products/restore-stock/{id}")
    public void restoreStock(@PathVariable Long id, @RequestBody Integer quantity);

}
