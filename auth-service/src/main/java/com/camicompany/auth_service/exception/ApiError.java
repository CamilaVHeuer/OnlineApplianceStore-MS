package com.camicompany.auth_service.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "Standard error response")
public record ApiError(@Schema(example = "400")
                       int status,
                       @Schema(example = "BAD_REQUEST")
                       String error,
                       @Schema(example = "Validation failed")
                       String message,
                       @Schema(
                               description = "Extra information about the error",
                               example = "{\"password\":\"must be at least 8 characters\"}"
                       )
                       Object details,
                       @Schema(example = "/auth/register")
                       String path,
                       @Schema(example = "2026-02-01T12:00:00")
                       LocalDateTime timestamp)  {
}
