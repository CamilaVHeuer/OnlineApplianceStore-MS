package com.camicompany.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @Schema(example = "user1")
        @NotBlank(message = "Username is required")
        String username,
        @Schema(example = "user1Password")
        @NotBlank (message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                message = "Password must contain letters and numbers"
        )
        String password
        ) {
}
