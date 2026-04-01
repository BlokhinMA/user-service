package ru.mikhailblokhin.userservice.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mikhailblokhin.userservice.exceptions.NoContentException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_MSG = "error";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_MSG, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Map<String, String>> handleOk(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_MSG, e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
