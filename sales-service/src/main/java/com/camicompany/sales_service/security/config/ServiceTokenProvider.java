package com.camicompany.sales_service.security.config;

import com.camicompany.sales_service.dto.LoginRequestDTO;
import com.camicompany.sales_service.security.config.feign.IAuthServiceClient;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter //to have available getToken method to use in another class
public class ServiceTokenProvider {
    @Value("${app.service.username}")
    private String serviceUsername;

    @Value("${app.service.password}")
    private String servicePassword;

    private final IAuthServiceClient authClient;
    private String token;

    public ServiceTokenProvider(IAuthServiceClient authClient) {
        this.authClient = authClient;
    }

    @PostConstruct
    public void init() {
        this.token = obtainToken();
    }

    // Get token to run sales-service
    private String obtainToken() {
        return authClient.login(new LoginRequestDTO(serviceUsername, servicePassword))
                .token();
    }


}

