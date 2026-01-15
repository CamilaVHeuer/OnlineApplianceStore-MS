package com.camicompany.sales_service.dto;

import com.camicompany.sales_service.model.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDTO {
    @Schema(description = "Unique identifier of the Sale.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long saleId;
    @Schema(description = "Date of the Sale.", example = "2025-06-15")
    private LocalDate date;
    @Schema(description = "Identifier of the associated Shopping Cart.", example = "1")
    private Long cartId;
    @Schema(description = "Total amount of the Sale.", example = "2500.00", accessMode = Schema.AccessMode.READ_ONLY)
    private Double totalAmount;
    @Schema(description = "Status of the Sale.", example = "CREATED", accessMode = Schema.AccessMode.READ_ONLY)
    private SaleStatus status;
}
