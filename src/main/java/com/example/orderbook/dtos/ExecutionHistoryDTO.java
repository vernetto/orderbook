package com.example.orderbook.dtos;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel(description="History of orders matched by executions")
public class ExecutionHistoryDTO {
    private Long id;

    private OrderEntryDTO orderEntry;
    private ExecutionDTO execution;
    private BigDecimal filledQuantity;
    private BigDecimal filledPrice;


    public ExecutionHistoryDTO() {
    }

    public ExecutionHistoryDTO(ExecutionDTO execution, OrderEntryDTO orderEntry, BigDecimal filledQuantity, BigDecimal filledPrice) {
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

    public OrderEntryDTO getOrderEntry() {
        return orderEntry;
    }

    public void setOrderEntry(OrderEntryDTO orderEntry) {
        this.orderEntry = orderEntry;
    }

    public ExecutionDTO getExecution() {
        return execution;
    }

    public void setExecution(ExecutionDTO execution) {
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
