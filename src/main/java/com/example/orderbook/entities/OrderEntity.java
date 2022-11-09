package com.example.orderbook.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class OrderEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String financialInstrumendId;
    @Column
    private BigDecimal quantity;
    @Column
    private LocalDate entryDate;
    @Column
    private OrderType orderType;
    @Column
    private BigDecimal price;


    @ManyToOne
    private OrderBookEntity orderBookEntity;


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public String getFinancialInstrumendId() {
        return financialInstrumendId;
    }

    public void setFinancialInstrumendId(String financialInstrumendId) {
        this.financialInstrumendId = financialInstrumendId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderBookEntity getOrderBook() {
        return orderBookEntity;
    }

    public void setOrderBook(OrderBookEntity orderBookEntity) {
        this.orderBookEntity = orderBookEntity;
    }
}
