package com.example.orderbook.repositories;

import com.example.orderbook.entities.OrderBook;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderBookRepository extends CrudRepository<OrderBook, Long> {
    Optional<OrderBook> findFirstByOrderByIdDesc();
}
