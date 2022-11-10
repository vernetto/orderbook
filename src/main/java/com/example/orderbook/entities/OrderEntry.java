package com.example.orderbook.entities;

import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.constants.OrderType;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@ApiModel(description="Order to be filled by executions")
public class OrderEntry {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 50, nullable = false)
    private String financialInstrumendId;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Column(nullable = false)
    private LocalDateTime entryDate;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderEntryStatus status;

    public OrderEntry(Long id, String financialInstrumendId, BigDecimal quantity, LocalDateTime entryDate, OrderType orderType, BigDecimal price, OrderEntryStatus status) {
        this.id = id;
        this.financialInstrumendId = financialInstrumendId;
        this.quantity = quantity;
        this.entryDate = entryDate;
        this.orderType = orderType;
        this.price = price;
        this.status = status;
    }

    public OrderEntry() {

    }

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

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntry that = (OrderEntry) o;
        return id.equals(that.id) && financialInstrumendId.equals(that.financialInstrumendId) && quantity.equals(that.quantity) && entryDate.equals(that.entryDate) && orderType == that.orderType && price.equals(that.price) && status == that.status;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrderEntry{");
        sb.append("id=").append(id);
        sb.append(", financialInstrumendId='").append(financialInstrumendId).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", entryDate=").append(entryDate);
        sb.append(", orderType=").append(orderType);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, financialInstrumendId, quantity, entryDate, orderType, price, status);
    }

    public OrderEntryStatus getStatus() {
        return status;
    }

    public void setStatus(OrderEntryStatus status) {
        this.status = status;
    }


}
