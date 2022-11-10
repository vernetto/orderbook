package com.example.orderbook;

import com.example.orderbook.entities.OrderBook;
import com.example.orderbook.entities.OrderBookStatus;
import com.example.orderbook.repositories.OrderBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Initialises database
 */
@Component
public class DataLoader implements ApplicationRunner {

    private OrderBookRepository orderBookRepository;

    @Autowired
    public DataLoader(OrderBookRepository orderBookRepository) {
        this.orderBookRepository = orderBookRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        OrderBook orderBook = new OrderBook();
        orderBook.setStatus(OrderBookStatus.OPEN);
        orderBookRepository.save(orderBook);
    }
}
