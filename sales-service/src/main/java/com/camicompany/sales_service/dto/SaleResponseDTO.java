package com.camicompany.sales_service.dto;

import com.camicompany.sales_service.model.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;


import java.time.LocalDate;

public record SaleResponseDTO(
    @Schema(description = "Unique identifier of the Sale.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long saleId,
    @Schema(description = "Date of the Sale.", example = "2025-06-15")
    LocalDate date,
    @Schema(description = "Username of the user who owns this cart", example ="user1", accessMode = Schema.AccessMode.READ_ONLY)
    String username,
    @Schema(description = "Identifier of the associated Shopping Cart.", example = "1")
    Long cartId,
    @Schema(description = "Total amount of the Sale.", example = "2500.00", accessMode = Schema.AccessMode.READ_ONLY)
    Double totalAmount,
    @Schema(description = "Status of the Sale.", example = "CREATED", accessMode = Schema.AccessMode.READ_ONLY)
    SaleStatus status){
}
