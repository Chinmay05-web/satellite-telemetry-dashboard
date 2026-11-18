
package com.satellite.dashboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;
import com.satellite.dashboard.exception.DuplicateEmailException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> createErrorResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> error = new LinkedHashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);

        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> validationErrors = new LinkedHashMap<>();

        for (FieldError fieldError :
                exception.getBindingResult().getFieldErrors()) {

            validationErrors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        Map<String, Object> response =
                createErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        "Validation failed"
                );

        response.put("validationErrors", validationErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
@ExceptionHandler(DuplicateEmailException.class)
public ResponseEntity<Map<String, Object>> handleDuplicateEmail(
        DuplicateEmailException exception) {

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(createErrorResponse(
                    HttpStatus.CONFLICT,
                    exception.getMessage()
            ));
}

@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<Map<String, Object>> handleBadCredentials(
        BadCredentialsException exception) {

    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(createErrorResponse(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            ));
}
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NoSuchElementException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(
                        HttpStatus.NOT_FOUND,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception exception) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected server error occurred"
                ));
    }
}
