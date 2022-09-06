package com.nurtech.orderservice.controller;

import com.nurtech.orderservice.dto.OrderRequest;
import com.nurtech.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {


    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String orderPlace(@RequestBody OrderRequest orderRequest) {
        orderService.orderPlace(orderRequest);
        return "order success placed";
    }
}
