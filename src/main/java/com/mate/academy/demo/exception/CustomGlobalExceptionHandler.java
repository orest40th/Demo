package com.mate.academy.demo.exception;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(e -> {
                    if (e instanceof FieldError) {
                        String field = ((FieldError) e).getField();
                        String message = e.getDefaultMessage();

                        return field + ' ' + message;
                    }

                    return e.getDefaultMessage();
                })
                .toList();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("message", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND);
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class) // Catch all unhandled exceptions
    public ResponseEntity<Map<String,Object>> handleGeneralException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
