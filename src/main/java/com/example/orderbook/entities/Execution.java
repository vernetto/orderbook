package com.example.orderbook.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Execution {
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
    @Enumerated(EnumType.STRING)
    private ExecutionType executionType;


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
