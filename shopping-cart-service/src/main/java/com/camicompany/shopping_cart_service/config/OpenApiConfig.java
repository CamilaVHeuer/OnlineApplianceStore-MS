package com.camicompany.shopping_cart_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI CartOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shopping Cart Service API")
                        .version("1.0")
                        .description("""
                                Microservice responsible for managing shopping carts in an e-commerce system.""")
                        .contact(new Contact()
                                .name("Camila Villalba Heuer")
                                .email("cbvillalbaheuer@gmail.com")
                                .url("https://github.com/CamilaVHeuer")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8082")
                                .description("Shopping Cart Service - Local"),
                        new Server()
                                .url("http://localhost:8000/shopping-cart-service")
                                .description("API Gateway")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth")
                )
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));
    }
}








