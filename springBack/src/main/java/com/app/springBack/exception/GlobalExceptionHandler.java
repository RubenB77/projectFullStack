package com.app.springBack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle cases where a specific HTTP status code is thrown
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<LinkedHashMap<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put("status", String.valueOf(ex.getStatusCode().value()));
        response.put("error", ex.getReason());
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<LinkedHashMap<String, String>> handleGenericException(Exception ex, WebRequest request) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put("status", "500");
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle validation errors (if using validation annotations in @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<LinkedHashMap<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put("status", "400"); // Add status code to the response

        // Collect validation errors
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            response.put(error.getField(), error.getDefaultMessage())
        );

        response.put("error", "Validation Failed");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
