package com.camicompany.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // API routes
                .route("products", r -> r.path("/api/products/**")
                        .uri("lb://products-service"))
                .route("cart", r -> r.path("/api/cart/**")
                        .uri("lb://shopping-cart-service"))
                .route("sales", r -> r.path("/api/sales/**")
                        .uri("lb://sales-service"))

                // Swagger documentation routes
                .route("products-docs", r -> r.path("/products-service/v3/api-docs")
                        .uri("lb://products-service/v3/api-docs"))
                .route("cart-docs", r -> r.path("/shopping-cart-service/v3/api-docs")
                        .uri("lb://shopping-cart-service/v3/api-docs"))
                .route("sales-docs", r -> r.path("/sales-service/v3/api-docs")
                        .uri("lb://sales-service/v3/api-docs"))
                .build();
    }
}
