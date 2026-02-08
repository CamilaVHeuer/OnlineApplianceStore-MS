package com.camicompany.sales_service.security.config.feign;

import com.camicompany.sales_service.dto.LoginRequestDTO;
import com.camicompany.sales_service.dto.TokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="auth-service")
public interface IAuthServiceClient {
    @PostMapping("api/auth/login")
    TokenResponseDTO login(@RequestBody LoginRequestDTO request);
}
