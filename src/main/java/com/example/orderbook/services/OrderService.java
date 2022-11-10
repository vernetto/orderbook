package com.example.orderbook.services;

import com.example.orderbook.entities.OrderEntry;
import com.example.orderbook.entities.OrderBook;
import com.example.orderbook.exceptions.OrderBookException;
import com.example.orderbook.repositories.OrderBookRepository;
import com.example.orderbook.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private OrderRepository orderRepository;
    private OrderBookRepository orderBookRepository;

    public OrderService(OrderRepository orderRepository, OrderBookRepository orderBookRepository) {
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
    }

    public OrderEntry createOrder(OrderEntry orderEntry) throws OrderBookException {
        OrderBook orderBook = orderBookRepository.findById(1L).orElseThrow(() -> new OrderBookException("no order book available, unable to add order"));
        if (orderBook.isClosed()) {
            throw new OrderBookException("order book is closed, unable to add order");
        }
        orderRepository.save(orderEntry);
        logger.info("order successfully saved " + orderEntry);
        return orderEntry;
    }
}
