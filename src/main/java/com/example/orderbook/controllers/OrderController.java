package com.example.orderbook.controllers;

import com.example.orderbook.converters.EntityDTOConverter;
import com.example.orderbook.dtos.ExecutionDTO;
import com.example.orderbook.dtos.ExecutionHistoryDTO;
import com.example.orderbook.dtos.OrderEntryDTO;
import com.example.orderbook.entities.ExecutionHistory;
import com.example.orderbook.entities.OrderEntry;
import com.example.orderbook.exceptions.OrderBookException;
import com.example.orderbook.services.OrderService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Order Rest Controller", description = "REST API for Order")
@RequestMapping("/")
@RestController
public class OrderController {

    private final OrderService orderService;
    private final EntityDTOConverter entityDTOConverter;

    public OrderController(OrderService orderService, EntityDTOConverter entityDTOConverter) {
        this.orderService = orderService;
        this.entityDTOConverter = entityDTOConverter;
    }

    @PostMapping("/createOrder")
    public OrderEntryDTO createOrder(@RequestBody OrderEntryDTO orderEntryDTO) throws OrderBookException {
        OrderEntry orderEntry = orderService.createOrder(entityDTOConverter.convertOrderEntryDTOToEntity(orderEntryDTO));
        return entityDTOConverter.convertOrderEntryEntityToDTO(orderEntry);
    }

    @DeleteMapping("/deleteOrder")
    public void deleteOrder(@RequestParam long id) {
        orderService.deleteOrder(id);
    }

    @PutMapping("/updateOrder/{id}")
    public void updateOrder(@PathVariable("id") Long id, @RequestBody OrderEntryDTO orderEntryDTO) throws OrderBookException {
        OrderEntry orderEntry = orderService.createOrder(entityDTOConverter.convertOrderEntryDTOToEntity(orderEntryDTO));
        orderService.updateOrder(id, orderEntry);
    }

    @PostMapping("/closeOrderBook")
    public void closeOrderBook() throws OrderBookException {
        orderService.closeOrderBook();
    }

    @PostMapping("/processExecution")
    public List<ExecutionHistoryDTO> processExecution(@RequestBody ExecutionDTO executionDTO) throws OrderBookException {
        // hack here, to avoid persistence issues...
        executionDTO.setId(null);
        List<ExecutionHistory> executionHistory = orderService.processExecution(entityDTOConverter.convertExecutionDTOToEntity(executionDTO));
        // If all orders have been completed, a simple “execution report” shall be presented
        if (orderService.allOrdersCompleted()) {
            orderService.closeAllFilledOrders();
        }
        return entityDTOConverter.convertExecutionHistoryEntityToDTOList(executionHistory);
    }

    @PostMapping("/openOrderBook")
    public void openOrderBook() throws OrderBookException {
        orderService.openOrderBook();
    }

    @GetMapping("/currentState")
    public void getCurrentState() {
        // TODO
    }

    @GetMapping("/executionReport")
    public void getExecutionReport() {
        // TODO
        orderService.generateExecutionReport();
    }




}
