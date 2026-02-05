package com.camicompany.auth_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePasswordDTO( @Schema(example = "user1NePassword")
                                 @Size(min = 8, message = "Password must be at least 8 characters")
                                 @Pattern(
                                         regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                                         message = "Password must contain letters and numbers"
                                 )
                                 @NotBlank String newPassword) {
}
