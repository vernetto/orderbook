package com.example.orderbook.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderBook {
    private Long id;
    private OrderBookStatus status;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public OrderBookStatus getStatus() {
        return status;
    }

    public void setStatus(OrderBookStatus orderBookStatus) {
        this.status = orderBookStatus;
    }


}
