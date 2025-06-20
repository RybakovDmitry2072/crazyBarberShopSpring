package org.example.springcrazybarbershop.exceptions.handlers;

import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.exceptions.NotFoundEmployeeException;
import org.example.springcrazybarbershop.exceptions.SmsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.error("Method not allowed error: {}", ex.getMessage());
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        body.put("error", "Method Not Allowed");
        body.put("message", ex.getMessage());
        body.put("supportedMethods", ex.getSupportedHttpMethods());
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(body);
    }


    @ExceptionHandler(NotFoundEmployeeException.class)
    public ResponseEntity<Map<String, Object>> handleNoBarbers(NotFoundEmployeeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File processing error: " + e.getMessage());
    }

    @ExceptionHandler(SmsException.class)
    public ResponseEntity<Map<String, Object>> handleSmsException(SmsException ex) {
        log.error("SMS error occurred: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "SMS sending failed");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(response);
    }
}
