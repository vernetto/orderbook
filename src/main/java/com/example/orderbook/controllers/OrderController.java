package com.example.orderbook.controllers;

import com.example.orderbook.entities.OrderEntry;
import com.example.orderbook.exceptions.OrderBookException;
import com.example.orderbook.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
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
    @Operation(description = "Creates now order",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = OrderEntry.class), mediaType = MediaType.APPLICATION_JSON_VALUE), required = true),
        responses = {
            @ApiResponse(responseCode = "200",content = @Content(schema = @Schema(implementation = OrderEntry.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
        })
    public OrderEntry createOrder(@RequestBody OrderEntry orderEntry) throws OrderBookException {
        return orderService.createOrder(orderEntry);
    }

}
