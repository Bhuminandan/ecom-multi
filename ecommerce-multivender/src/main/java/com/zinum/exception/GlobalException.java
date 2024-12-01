package com.zinum.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> sellerException(SellerException se, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(se.getMessage());
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> productException(ProductException pe, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(pe.getMessage());
        errorDetails.setDetails(request.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return ResponseEntity.badRequest().body(errorDetails);
    }
}
