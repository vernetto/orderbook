package com.example.orderbook.dtos;

import com.example.orderbook.constants.OrderBookStatus;

public class OrderBookDTO {
    private Long id;
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
