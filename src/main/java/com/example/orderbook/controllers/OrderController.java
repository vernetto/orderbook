package com.example.orderbook.controllers;

import com.example.orderbook.entities.OrderEntry;
import com.example.orderbook.exceptions.OrderBookException;
import com.example.orderbook.services.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/createOrder")
    public OrderEntry createOrder(@RequestBody OrderEntry orderEntry) throws OrderBookException {
        return orderService.createOrder(orderEntry);
    }

}
