package com.camicompany.sales_service.dto;

import com.camicompany.sales_service.model.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CreateSaleDTO(

        @Schema(description = "Date of the Sale.", example = "2025-06-15")
        @NotNull(message = "Sale date is required")
        @PastOrPresent(message = "Sale date cannot be in the future")
        LocalDate date,
        @Schema(description = "Identifier of the associated Shopping Cart.", example = "1")
        @NotNull(message = "Cart ID is required")
        Long cartId){
}

