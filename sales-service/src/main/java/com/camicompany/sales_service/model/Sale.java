package com.camicompany.sales_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;
    private LocalDate date;
    private Long cartId;
    private Double totalAmount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus status = SaleStatus.CREATED;

}
