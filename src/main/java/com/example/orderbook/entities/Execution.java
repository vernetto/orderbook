package com.example.orderbook.entities;

import com.example.orderbook.constants.ExecutionType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Execution {
    @Id
    @GeneratedValue(generator="execution_seq")
    @SequenceGenerator(name="execution_seq",sequenceName="EXECUTION_SEQ", allocationSize=1, initialValue = 1)
    private Long id;
    @Column(length = 50, nullable = false)
    private String financialInstrumentId;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionType executionType;
    @ManyToOne
    OrderBook orderBook;

    public Execution() {
    }

    public Execution(Long id, String financialInstrumentId, BigDecimal quantity, BigDecimal price, ExecutionType executionType) {
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

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Execution{");
        sb.append("id=").append(id);
        sb.append(", financialInstrumentId='").append(financialInstrumentId).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", price=").append(price);
        sb.append(", executionType=").append(executionType);
        sb.append(", orderBookId=").append(orderBook != null ? orderBook.getId() : "null");
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
