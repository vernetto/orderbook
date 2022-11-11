package com.example.orderbook.controllers;

import com.example.orderbook.entities.OrderEntry;
import com.example.orderbook.exceptions.OrderBookException;
import com.example.orderbook.services.OrderService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@Api(value = "Order Rest Controller", description = "REST API for Order")
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

    @DeleteMapping("/deleteOrder")
    public void deleteOrder(@RequestParam long id) throws OrderBookException {
        orderService.deleteOrder(id);
    }

    @PutMapping("/editOrder")
    public void updateOrder(@RequestBody OrderEntry orderEntry) throws OrderBookException {
        orderService.updateOrder(orderEntry);
    }

    @PostMapping("/closeOrderBook")
    public void closeOrderBook() throws OrderBookException {
        orderService.closeOrderBook();
    }


}
