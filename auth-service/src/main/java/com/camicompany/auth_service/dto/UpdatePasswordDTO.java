package com.camicompany.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDTO( @Schema(example = "user1NePassword")
                                 @NotBlank String newPassword) {
}
