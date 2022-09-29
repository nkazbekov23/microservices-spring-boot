package com.nurtech.orderservice.controller;

import com.nurtech.orderservice.dto.OrderRequest;
import com.nurtech.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {


    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
//    @CircuitBreaker(name = "inventory", fallbackMethod = "fallback")
//    @TimeLimiter(name = "inventory")
//    @Retry(name = "inventory")
    public String orderPlace(@RequestBody OrderRequest orderRequest) {
        return orderService.orderPlace(orderRequest);
       // return CompletableFuture.supplyAsync(() -> orderService.orderPlace(orderRequest));
    }


    public CompletableFuture<String> fallback(OrderRequest orderRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "oops something went wrong please order after some time");
    }
}
