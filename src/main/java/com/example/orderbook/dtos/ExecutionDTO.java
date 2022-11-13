package com.example.orderbook.dtos;

import com.example.orderbook.constants.ExecutionType;
import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel(description="Execution of a trade on a set of open OrderEntry")
public class ExecutionDTO {
    private Long id;
    private String financialInstrumentId;
    private BigDecimal quantity;
    private BigDecimal price;
    private ExecutionType executionType;
    OrderBookDTO orderBookDTO;

    public ExecutionDTO() {
    }

    public ExecutionDTO(Long id, String financialInstrumentId, BigDecimal quantity, BigDecimal price, ExecutionType executionType) {
        this.id = id;
        this.financialInstrumentId = financialInstrumentId;
        this.quantity = quantity;
        this.price = price;
        this.executionType = executionType;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public String getFinancialInstrumentId() {
        return financialInstrumentId;
    }

    public void setFinancialInstrumentId(String financialInstrumentId) {
        this.financialInstrumentId = financialInstrumentId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ExecutionType getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ExecutionType executionType) {
        this.executionType = executionType;
    }

    public OrderBookDTO getOrderBookDTO() {
        return orderBookDTO;
    }

    public void setOrderBookDTO(OrderBookDTO orderBookDTO) {
        this.orderBookDTO = orderBookDTO;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Execution{");
        sb.append("id=").append(id);
        sb.append(", financialInstrumentId='").append(financialInstrumentId).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", price=").append(price);
        sb.append(", executionType=").append(executionType);
        sb.append(", orderBookId=").append(orderBookDTO != null ? orderBookDTO.getId() : "null");
        sb.append('}');
        return sb.toString();
    }

    public boolean isOffer() {
        return ExecutionType.OFFER.equals(this.getExecutionType());
    }

    public boolean isAsk() {
        return ExecutionType.ASK.equals(this.getExecutionType());
    }

}
