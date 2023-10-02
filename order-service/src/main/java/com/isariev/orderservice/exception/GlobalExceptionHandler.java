package com.isariev.orderservice.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<String> handleConnectException(ConnectException ex) {
        // Customize the response, status code, and error message here
        return new ResponseEntity<>("Server unavailable: " + ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleConnectException(WebClientResponseException ex) {
        // Customize the response, status code, and error message here
        return new ResponseEntity<>("Server unavailable: " + ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}