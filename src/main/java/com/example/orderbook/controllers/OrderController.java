package com.example.orderbook.controllers;

import com.example.orderbook.converters.EntityDTOConverter;
import com.example.orderbook.dtos.ExecutionDTO;
import com.example.orderbook.dtos.ExecutionHistoryDTO;
import com.example.orderbook.dtos.OrderBookStateDTO;
import com.example.orderbook.dtos.OrderEntryDTO;
import com.example.orderbook.entities.Execution;
import com.example.orderbook.entities.ExecutionHistory;
import com.example.orderbook.entities.OrderBook;
import com.example.orderbook.entities.OrderEntry;
import com.example.orderbook.exceptions.ExceptionCode;
import com.example.orderbook.exceptions.OrderBookException;
import com.example.orderbook.reporting.ExecutionReportPDFGenerator;
import com.example.orderbook.services.OrderService;
import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Api(value = "Order Rest Controller", description = "REST API for Order")
@RequestMapping("/order/")
@RestController
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final EntityDTOConverter entityDTOConverter;
    private final ExecutionReportPDFGenerator executionReportPDFGenerator;

    public OrderController(OrderService orderService, EntityDTOConverter entityDTOConverter, ExecutionReportPDFGenerator executionReportPDFGenerator) {
        this.orderService = orderService;
        this.entityDTOConverter = entityDTOConverter;
        this.executionReportPDFGenerator = executionReportPDFGenerator;
    }

    @ApiOperation(value = "Creates new order", notes = "OrderBook must be open.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = OrderEntryDTO.class),
            @ApiResponse(code = 500, message = "System error or OrderBook closed")})
    @PostMapping("/v1/createOrder")
    public OrderEntryDTO createOrder(@RequestBody OrderEntryDTO orderEntryDTO) throws OrderBookException {
        OrderEntry orderEntry = orderService.createOrder(entityDTOConverter.convertOrderEntryDTOToEntity(orderEntryDTO));
        return entityDTOConverter.convertOrderEntryEntityToDTO(orderEntry);
    }

    @ApiOperation(value = "Deletes order by orderId", notes = "OrderBook must be open.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Order does not exist"),
            @ApiResponse(code = 500, message = "System error")})
    @DeleteMapping("/v1/deleteOrder")
    public void deleteOrder(@RequestParam long id) {
        try {
            orderService.deleteOrder(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order id not found " + id, e);
        }
    }

    @ApiOperation(value = "Updates order by orderId", notes = "OrderBook must be open.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System error, Order does not exist or Order is not OPEN")})
    @PutMapping("/v1/updateOrder/{id}")
    public void updateOrder(@PathVariable("id") Long id, @RequestBody OrderEntryDTO orderEntryDTO) throws OrderBookException {
        OrderEntry orderEntry = entityDTOConverter.convertOrderEntryDTOToEntity(orderEntryDTO);
        orderService.updateOrder(id, orderEntry);
    }


    @ApiOperation(value = "close Order book", notes = "OrderBook must be open.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System error, or Order book is already closed")})
    @PostMapping("/v1/closeOrderBook")
    public void closeOrderBook() throws OrderBookException {
        orderService.closeOrderBook();
    }

    @ApiOperation(value = "Processes an execution against all open orders in the current OrderBook", notes = "OrderBook must be closed.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System error, or Order book is open")})
    @PostMapping("/v1/processExecution")
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

    @ApiOperation(value = "Open a new orderBook", notes = "OrderBook must be closed.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System error, or Order book is already open")})
    @PostMapping("/v1/openOrderBook")
    public void openOrderBook() throws OrderBookException {
        orderService.openOrderBook();
    }

    @ApiOperation(value = "Return current Orders, Execution and ExecutionHistory items", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System error")})
    @GetMapping("/v1/currentState")
    public OrderBookStateDTO getCurrentState() throws OrderBookException {
        OrderBookStateDTO orderBookStateDTO = new OrderBookStateDTO();
        OrderBook orderBook = orderService.getOrderBook();
        orderBookStateDTO.setOrderBookDTO(entityDTOConverter.convertOrderBookEntityToDTO(orderBook));
        List<Execution> executionList = orderService.getExecutions(orderBook);
        orderBookStateDTO.setExecutionDTOList(entityDTOConverter.convertExecutionListEntityToDTO(executionList));

        List<OrderEntry> orderEntryList = orderService.getOrderEntries(orderBook);
        orderBookStateDTO.setOrderEntryDTOList(entityDTOConverter.convertOrderBookListEntityToDTO(orderEntryList));

        List<ExecutionHistory> executionHistoryList = orderService.getExecutionHistory(orderBook);
        orderBookStateDTO.setExecutionHistoryDTOList(entityDTOConverter.convertExecutionHistoryEntityToDTOList(executionHistoryList));

        return orderBookStateDTO;
    }

    @ApiOperation(value = "Return list of Orders filling by executions", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System error")})
    @GetMapping("/v1/executionReport")
    public List<ExecutionHistoryDTO> getExecutionReport() throws OrderBookException {
        OrderBook orderBook = orderService.getOrderBook();
        List<ExecutionHistory> executionHistoryList = orderService.getExecutionHistory(orderBook);
        return entityDTOConverter.convertExecutionHistoryEntityToDTOList(executionHistoryList);
    }

    @ApiOperation(value = "Return list of Orders filling by executions as PDF", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "System error")})
    @GetMapping(value = "/v1/executionReportPDF", produces = "application/pdf")
    public ResponseEntity<byte[]> getExecutionReportPDF() throws OrderBookException {
        OrderBook orderBook = orderService.getOrderBook();
        List<ExecutionHistory> executionHistoryList = orderService.getExecutionHistory(orderBook);
        List<ExecutionHistoryDTO> executionHistoryDTOList = entityDTOConverter.convertExecutionHistoryEntityToDTOList(executionHistoryList);
        byte[] contents;
        try {
            contents = executionReportPDFGenerator.generateReport(executionHistoryDTOList);
        } catch (DocumentException e) {
            logger.error("error generating PDF", e);
            throw new OrderBookException(ExceptionCode.ERR_006, e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "executionReport_" + orderBook.getId() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }


}
