package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankingException.class)
    public ResponseEntity<Object> handleBankingException(BankingException ex) {
        return buildResponse(ex.getMessage(), ex.getStatusCode());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSqlException(SQLException ex) {
        // Don't leak raw SQL error details to the client — log them server-side instead
        System.err.println("Unhandled SQLException: " + ex.getMessage());
        return buildResponse("A database error occurred.", 500);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        System.err.println("Unhandled exception: " + ex.getMessage());
        return buildResponse("An unexpected error occurred.", 500);
    }

    private ResponseEntity<Object> buildResponse(String message, int statusCode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", statusCode);
        body.put("error", message);

        return ResponseEntity.status(HttpStatus.valueOf(statusCode)).body(body);
    }

}