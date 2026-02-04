package com.camicompany.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @Schema(example = "user1")
        @NotBlank(message = "Username is required")
        String username,
        @Schema(example = "user1Password")
        @NotBlank (message = "Password is required")
        String password
        ) {
}
