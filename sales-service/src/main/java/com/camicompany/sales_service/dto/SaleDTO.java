package com.camicompany.sales_service.dto;

import com.camicompany.sales_service.model.SaleStatus;
import lombok.*;

import java.time.LocalDate;
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDTO {
    private Long saleId;
    private LocalDate date;
    private Long cartId;
    private Double totalAmount;
    private SaleStatus status;
}
