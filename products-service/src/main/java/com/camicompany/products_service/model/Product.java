package com.camicompany.products_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Column(unique = true, nullable = false)
    private String code;
    private String name;
    private String brand;
    private Double unitPrice;
    private Integer stock;
}
