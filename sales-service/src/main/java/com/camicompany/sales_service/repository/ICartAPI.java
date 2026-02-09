package com.camicompany.sales_service.repository;

import com.camicompany.sales_service.dto.CartDTO;
import com.camicompany.sales_service.security.config.feign.FeignAuthServiceInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="shopping-cart-service",
        configuration = FeignAuthServiceInterceptor.class)
public interface ICartAPI {
    @GetMapping("/api/cart/internal/{id}")
    public CartDTO getCartById(@PathVariable ("id") Long cardId);

    @PutMapping("/api/cart/mark-as-sold/{id}")
    public void markCartAsSold(@PathVariable("id") Long cartId);

}
