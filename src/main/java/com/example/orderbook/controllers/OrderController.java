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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    @PostMapping("/v1/createOrder")
    public OrderEntryDTO createOrder(@RequestBody OrderEntryDTO orderEntryDTO) throws OrderBookException {
        OrderEntry orderEntry = orderService.createOrder(entityDTOConverter.convertOrderEntryDTOToEntity(orderEntryDTO));
        return entityDTOConverter.convertOrderEntryEntityToDTO(orderEntry);
    }

    @DeleteMapping("/v1/deleteOrder")
    public void deleteOrder(@RequestParam long id) {
        orderService.deleteOrder(id);
    }

    @PutMapping("/v1/updateOrder/{id}")
    public void updateOrder(@PathVariable("id") Long id, @RequestBody OrderEntryDTO orderEntryDTO) throws OrderBookException {
        OrderEntry orderEntry = entityDTOConverter.convertOrderEntryDTOToEntity(orderEntryDTO);
        orderService.updateOrder(id, orderEntry);
    }

    @PostMapping("/v1/closeOrderBook")
    public void closeOrderBook() throws OrderBookException {
        orderService.closeOrderBook();
    }

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

    @PostMapping("/v1/openOrderBook")
    public void openOrderBook() throws OrderBookException {
        orderService.openOrderBook();
    }

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

    @GetMapping("/v1/executionReport")
    public List<ExecutionHistoryDTO> getExecutionReport() throws OrderBookException {
        OrderBook orderBook = orderService.getOrderBook();
        List<ExecutionHistory> executionHistoryList = orderService.getExecutionHistory(orderBook);
        return entityDTOConverter.convertExecutionHistoryEntityToDTOList(executionHistoryList);
    }

    @GetMapping(value = "/v1/executionReportPDF", produces = "application/pdf")
    public ResponseEntity<byte[]> getExecutionReportPDF() throws OrderBookException {
        OrderBook orderBook = orderService.getOrderBook();
        List<ExecutionHistory> executionHistoryList = orderService.getExecutionHistory(orderBook);
        List<ExecutionHistoryDTO> executionHistoryDTOList = entityDTOConverter.convertExecutionHistoryEntityToDTOList(executionHistoryList);
        byte[] contents;
        try {
            contents = executionReportPDFGenerator.generateReport(executionHistoryDTOList);
        }
        catch (DocumentException e) {
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
