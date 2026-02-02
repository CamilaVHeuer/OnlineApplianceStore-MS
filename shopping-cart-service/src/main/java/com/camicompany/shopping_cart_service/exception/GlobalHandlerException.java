package com.camicompany.shopping_cart_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalHandlerException {

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex) {
            ApiError error = new ApiError(ex.getStatusCode().value(),
                    ex.getStatusCode().toString(),
                    ex.getReason(),
                    LocalDateTime.now());
            return ResponseEntity.status(ex.getStatusCode()).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {

            String message = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .findFirst()
                    .orElse("Validation error");

            ApiError error = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.toString(),
                    message,
                    LocalDateTime.now()
            );

            return ResponseEntity.badRequest().body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGenericException(Exception ex) {

            ApiError error = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    "Unexpected error",
                    LocalDateTime.now()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

