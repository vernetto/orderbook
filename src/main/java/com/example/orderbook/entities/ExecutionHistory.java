package com.example.orderbook.entities;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@ApiModel(description="History of orders matched by executions")
public class ExecutionHistory {
    @Id
    @GeneratedValue(generator="executionhistory_seq")
    @SequenceGenerator(name="executionhistory_seq",sequenceName="EXECUTIONHISTORY_SEQ", allocationSize=1, initialValue = 1)
    private Long id;

    @ManyToOne
    private OrderEntry orderEntry;
    @ManyToOne
    private Execution execution;

    @Column(nullable = false)
    private BigDecimal filledQuantity;
    @Column(nullable = false)
    private BigDecimal filledPrice;


    public ExecutionHistory() {
    }

    public ExecutionHistory(Execution execution, OrderEntry orderEntry, BigDecimal filledQuantity, BigDecimal filledPrice) {
        this.setExecution(execution);
        this.setOrderEntry(orderEntry);
        this.setFilledQuantity(filledQuantity);
        this.setFilledPrice(filledPrice);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderEntry getOrderEntry() {
        return orderEntry;
    }

    public void setOrderEntry(OrderEntry orderEntry) {
        this.orderEntry = orderEntry;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public BigDecimal getFilledQuantity() {
        return filledQuantity;
    }

    public void setFilledQuantity(BigDecimal filledQuantity) {
        this.filledQuantity = filledQuantity;
    }

    public BigDecimal getFilledPrice() {
        return filledPrice;
    }

    public void setFilledPrice(BigDecimal filledPrice) {
        this.filledPrice = filledPrice;
    }
}
