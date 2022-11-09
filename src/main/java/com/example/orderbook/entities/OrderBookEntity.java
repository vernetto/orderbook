package com.example.orderbook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderBookEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private OrderBookStatus status;

    public void setId(Long id) {
        this.id = id;
    }


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
