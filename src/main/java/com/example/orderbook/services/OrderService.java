package com.example.orderbook.services;

import com.example.orderbook.constants.ExecutionType;
import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.constants.OrderType;
import com.example.orderbook.entities.Execution;
import com.example.orderbook.entities.OrderEntry;
import com.example.orderbook.entities.OrderBook;
import com.example.orderbook.exceptions.OrderBookException;
import com.example.orderbook.processors.OrderProcessor;
import com.example.orderbook.repositories.OrderBookRepository;
import com.example.orderbook.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private OrderRepository orderRepository;
    private OrderBookRepository orderBookRepository;

    private OrderProcessor orderProcessor;

    public OrderService(OrderRepository orderRepository, OrderBookRepository orderBookRepository, OrderProcessor orderProcessor) {
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.orderProcessor = orderProcessor;
    }

    public OrderEntry createOrder(OrderEntry orderEntry) throws OrderBookException {
        logger.info("creating order " + orderEntry);
        OrderBook orderBook = getOrderBook();
        if (orderBook.isClosed()) {
            throw new OrderBookException("order book is closed, unable to add order");
        }
        orderRepository.save(orderEntry);
        logger.info("order successfully saved " + orderEntry);
        return orderEntry;
    }

    public OrderBook getOrderBook() throws OrderBookException {
        OrderBook orderBook = orderBookRepository.findById(1L).orElseThrow(() -> new OrderBookException("no order book available, unable to add order"));
        return orderBook;
    }

    public List<OrderEntry> processExecution(Execution execution) {
        logger.info("processing execution " + execution);
        OrderType orderType = execution.getExecutionType().equals(ExecutionType.OFFER) ? OrderType.BUY : OrderType.SELL;
        List<OrderEntry> orders = orderRepository.findByStatusAndFinancialInstrumendIdAndOrderTypeOrderByEntryDateAsc(OrderEntryStatus.OPEN, execution.getFinancialInstrumendId(), orderType);
        logger.info("these orders are matching the instrument : " + orders);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders);
        logger.info("these orders have been affected by the execution : " + affectedOrders);
        return affectedOrders;
    }


}
