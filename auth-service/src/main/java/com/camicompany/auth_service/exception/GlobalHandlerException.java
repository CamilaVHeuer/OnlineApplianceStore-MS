package com.camicompany.auth_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.exceptions.JWTVerificationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerException {

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex,
                        HttpServletRequest request) {
                ApiError apiError = new ApiError(
                                ex.getStatusCode().value(),
                                ex.getStatusCode().toString(),
                                ex.getReason(),
                                null,
                                request.getRequestURI(),
                                LocalDateTime.now());
                return ResponseEntity.status(ex.getStatusCode()).body(apiError);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult().getFieldErrors()
                                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

                ApiError apiError = new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.name(),
                                "Validation failed",
                                errors,
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.badRequest().body(apiError);
        }

        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<ApiError> handleUsernameNotFound(UsernameNotFoundException ex,
                        HttpServletRequest request) {
                ApiError apiError = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.name(),
                                ex.getMessage(),
                                null,
                                request.getRequestURI(),
                                LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
        }

        @ExceptionHandler(JWTVerificationException.class)
        public ResponseEntity<ApiError> handleJwtVerification(JWTVerificationException ex, HttpServletRequest request) {
                ApiError apiError = new ApiError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.name(),
                                ex.getMessage(),
                                null,
                                request.getRequestURI(),
                                LocalDateTime.now());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
                ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.name(),
                                ex.getMessage(),
                                null,
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);

        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
                ApiError apiError = new ApiError(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                                "Unexpected error",
                                null,
                                request.getRequestURI(),
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);

        }
}
