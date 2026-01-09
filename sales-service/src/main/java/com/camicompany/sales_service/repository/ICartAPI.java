package com.camicompany.sales_service.repository;

import com.camicompany.sales_service.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="shopping-cart-service")
public interface ICartAPI {
    @GetMapping("/api/cart/{id}")
    public CartDTO getCartById(@PathVariable ("id") Long cardId);

}
