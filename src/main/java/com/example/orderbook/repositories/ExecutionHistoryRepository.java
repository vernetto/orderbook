package com.example.orderbook.repositories;

import com.example.orderbook.entities.ExecutionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionHistoryRepository extends JpaRepository<ExecutionHistory, Long>  {

}
