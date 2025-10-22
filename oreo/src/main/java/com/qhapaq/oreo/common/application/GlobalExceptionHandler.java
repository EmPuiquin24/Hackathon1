package com.qhapaq.oreo.common.application;

import com.qhapaq.oreo.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(ResponseStatusException e) {
        ErrorResponseDto response = new ErrorResponseDto();
        response.setError(e.getStatusCode().toString());
        response.setMessage(e.getReason());
        response.setTimestamp(Instant.now().toString());

        return new ResponseEntity<>(response, e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError firstError = e.getFieldErrors().getFirst();

        ErrorResponseDto response = new ErrorResponseDto();
        response.setError("BAD_REQUEST");
        response.setMessage(firstError.getField() + ": " + firstError.getDefaultMessage());
        response.setTimestamp(Instant.now().toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}