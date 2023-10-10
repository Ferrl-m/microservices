package com.isariev.orderservice.controller;

import com.isariev.orderservice.dto.OrderRequest;
import com.isariev.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    @CircuitBreaker(name = "order-service")
    @Retry(name = "order-service")
    @TimeLimiter(name = "order-service")
    public CompletionStage<Void> placeOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Attempting to place an order.");
        return CompletableFuture.runAsync(() -> {
            orderService.placeOrder(orderRequest);
            ResponseEntity.ok("order placed successfully");
        });
    }
}
