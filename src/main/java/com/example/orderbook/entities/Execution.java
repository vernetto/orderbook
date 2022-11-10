package com.example.orderbook.entities;

import com.example.orderbook.constants.ExecutionType;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@ApiModel(description="Execution of a trade on an OrderEntry")
public class Execution {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 50, nullable = false)
    private String financialInstrumendId;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionType executionType;


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
