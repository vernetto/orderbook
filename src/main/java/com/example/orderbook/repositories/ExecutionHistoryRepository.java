package com.example.orderbook.repositories;

import com.example.orderbook.entities.ExecutionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecutionHistoryRepository extends JpaRepository<ExecutionHistory, Long>  {

    List<ExecutionHistory> findByOrderBookId(Long id);
}
