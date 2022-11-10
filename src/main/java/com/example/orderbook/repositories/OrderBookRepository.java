package com.example.orderbook.repositories;

import com.example.orderbook.entities.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long>  {
}
