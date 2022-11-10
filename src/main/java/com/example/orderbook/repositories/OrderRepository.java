package com.example.orderbook.repositories;

import com.example.orderbook.entities.OrderEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  OrderRepository extends JpaRepository<OrderEntry, Long> {
}
