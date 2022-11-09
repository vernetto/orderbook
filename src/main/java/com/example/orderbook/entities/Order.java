package com.example.orderbook.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Order {
    private Long id;
    private String financialInstrumendId;
    private BigDecimal quantity;
    private LocalDate entryDate;
    private OrderType orderType;
    private BigDecimal price;

    private OrderBook orderBook;


    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }
}
