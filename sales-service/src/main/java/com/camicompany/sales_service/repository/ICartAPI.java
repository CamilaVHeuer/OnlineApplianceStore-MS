package com.camicompany.sales_service.repository;

import com.camicompany.sales_service.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="shopping-cart-service")
public interface ICartAPI {
    @GetMapping("/api/cart/{id}")
    public CartDTO getCartById(@PathVariable ("id") Long cardId);

    @PutMapping("/api/cart/mark-as-sold/{id}")
    public void markCartAsSold(@PathVariable("id") Long cartId);

}
