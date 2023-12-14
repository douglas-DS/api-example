package com.example.apiexample.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception,
                                                            HttpServletRequest request) {
        String message = exception.getBody().getDetail();
        String detailedMessage = null;

        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        if (!errors.isEmpty()) {
            ObjectError error = errors.get(0);
            FieldError fieldError = ((FieldError) error);
            message = String.format("Field [%s]: Invalid value [%s]", fieldError.getField(), fieldError.getRejectedValue());
            detailedMessage = error.getDefaultMessage();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", OffsetDateTime.now(ZoneOffset.UTC));
        response.put("status", exception.getStatusCode().value());
        response.put("error", ((HttpStatus) exception.getStatusCode()).getReasonPhrase());
        response.put("path", request.getRequestURI());
        response.put("message", message);
        if (detailedMessage != null) {
            response.put("detailedMessage", detailedMessage);
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
