package com.example.orderbook.repositories;

import com.example.orderbook.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  OrderRepository extends JpaRepository<OrderEntity, Long> {
}
