package com.example.orderbook.repositories;

import com.example.orderbook.entities.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecutionRepository extends JpaRepository<Execution, Long>  {

    List<Execution> findByOrderBookId(Long id);
}
