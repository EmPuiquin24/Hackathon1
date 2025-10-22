package com.qhapaq.oreo.configuration;

import com.qhapaq.oreo.mail.exception.MailDeliveryException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Exceptions
    @ExceptionHandler(MailDeliveryException.class)
    public ResponseEntity<ErrorResponse> handleConflict(MailDeliveryException ex, WebRequest request) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST, request);
    }


    // Error Response

    @Getter
        @Setter
        @AllArgsConstructor
        public static class ErrorResponse {
            private String error;
            private String message;
            private LocalDateTime timestamp;
            private int status;
            private String path;
        }

        private ResponseEntity<ErrorResponse> buildResponse(Exception ex, HttpStatus status, WebRequest request) {
            ErrorResponse errorResponse = new ErrorResponse(
                    status.getReasonPhrase(),
                    ex.getMessage(),
                    LocalDateTime.now(),
                    status.value(),
                    request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(status).body(errorResponse);
        }
}
