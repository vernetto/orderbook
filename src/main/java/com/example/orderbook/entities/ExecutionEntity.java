package com.example.orderbook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class ExecutionEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String financialInstrumendId;
    @Column
    private BigDecimal quantity;
    @Column
    private BigDecimal price;
    @Column
    private ExecutionType executionType;


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
