package com.example.orderbook.repositories;

import com.example.orderbook.entities.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionRepository extends JpaRepository<Execution, Long>  {

}
