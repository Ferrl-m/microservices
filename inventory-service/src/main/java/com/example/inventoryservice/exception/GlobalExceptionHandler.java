package com.example.inventoryservice.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<String> handleConnectException(ConnectException ex) {
        return new ResponseEntity<>("Connection refused: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}