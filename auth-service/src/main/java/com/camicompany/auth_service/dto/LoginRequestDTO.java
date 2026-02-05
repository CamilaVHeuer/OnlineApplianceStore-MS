package com.camicompany.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Schema(example="user1")
        @NotBlank String username,
        @Schema(example="user1Password")
        @NotBlank String password) {
}
