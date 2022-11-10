package com.example.orderbook.repositories;

import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.entities.OrderEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  OrderRepository extends JpaRepository<OrderEntry, Long> {
    List<OrderEntry> findByStatusAndFinancialInstrumendIdOrderByEntryDateAsc(OrderEntryStatus open, String financialInstrumendId);
}
