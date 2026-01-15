package com.camicompany.products_service.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI ProductOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Products Service API")
                        .version("1.0")
                        .description("""
                        Microservice responsible for product catalog and inventory management.

                        Public endpoints are exposed through the API Gateway.
                        Internal endpoints are intended for inter-service communication.
                        """)
                        .contact(new Contact()
                                .name("Camila Villalba Heuer")
                                .email("cbvillalbaheuer@gmail.com")
                                .url("https://github.com/CamilaVHeuer")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8083")
                                .description("Products Service - Local"),
                        new Server()
                                .url("http://localhost:8000/products-service")
                                .description("API Gateway")
                ));
    }
}
