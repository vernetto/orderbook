package com.example.orderbook.entities;

import com.example.orderbook.constants.OrderBookStatus;

import javax.persistence.*;

@Entity
public class OrderBook {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
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

    public boolean isOpen() {
        return OrderBookStatus.OPEN.equals(this.status);
    }

    public boolean isClosed() {
        return OrderBookStatus.CLOSED.equals(this.status);
    }

}
