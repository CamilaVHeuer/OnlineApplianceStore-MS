package com.camicompany.sales_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;


import java.time.LocalDate;

public record SaleDateDTO (
    @NotNull(message = "Sale date is required")
    @PastOrPresent(message = "Sale date cannot be in the future")
    @Schema(description = "Updated Date of the Sale.", example = "2025-06-15")
    LocalDate date){
}
