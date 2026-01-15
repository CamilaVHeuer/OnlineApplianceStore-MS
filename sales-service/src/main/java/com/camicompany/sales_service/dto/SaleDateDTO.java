package com.camicompany.sales_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDateDTO {
    @NotNull
    @Schema(description = "Date to filter sales by.", example = "2025-06-15")
    private LocalDate date;
}
