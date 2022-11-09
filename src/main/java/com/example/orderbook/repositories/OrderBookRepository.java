package com.example.orderbook.repositories;

import com.example.orderbook.entities.OrderBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBookRepository extends JpaRepository<OrderBookEntity, Long>  {
}
