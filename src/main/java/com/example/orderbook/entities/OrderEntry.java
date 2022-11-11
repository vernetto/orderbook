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
    @GeneratedValue(generator="orderentry_seq")
    @SequenceGenerator(name="orderentry_seq",sequenceName="ORDERENTRY_SEQ", allocationSize=1, initialValue = 1)
    private Long id;
    @Column(length = 50, nullable = false)
    private String financialInstrumendId;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Column(nullable = false)
    private BigDecimal availableQuantity;
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

    public OrderEntry(Long id, String financialInstrumendId, BigDecimal quantity, BigDecimal availableQuantity, LocalDateTime entryDate, OrderType orderType, BigDecimal price, OrderEntryStatus status) {
        this.id = id;
        this.financialInstrumendId = financialInstrumendId;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
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

    public BigDecimal getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(BigDecimal availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntry that = (OrderEntry) o;
        return id.equals(that.id) && financialInstrumendId.equals(that.financialInstrumendId) && quantity.equals(that.quantity) && availableQuantity.equals(that.availableQuantity) && entryDate.equals(that.entryDate) && orderType == that.orderType && price.equals(that.price) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, financialInstrumendId, quantity, availableQuantity, entryDate, orderType, price, status);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrderEntry{");
        sb.append("id=").append(id);
        sb.append(", financialInstrumendId='").append(financialInstrumendId).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", availableQuantity=").append(availableQuantity);
        sb.append(", entryDate=").append(entryDate);
        sb.append(", orderType=").append(orderType);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }

    public OrderEntryStatus getStatus() {
        return status;
    }

    public void setStatus(OrderEntryStatus status) {
        this.status = status;
    }

    public boolean isOpen() {
        return OrderEntryStatus.OPEN.equals(this.getStatus());
    }
    public boolean isFilled() {
        return OrderEntryStatus.FILLED.equals(this.getStatus());
    }
    public boolean isClosed() {
        return OrderEntryStatus.CLOSED.equals(this.getStatus());
    }

    public boolean isBuy() {
        return OrderType.BUY.equals(this.getOrderType());
    }

    public boolean isSell() {
        return OrderType.SELL.equals(this.getOrderType());
    }

    public boolean acceptOffer(BigDecimal executionPrice) {
        // order.price = 5, executionProce = 5.5 -> OK
        return this.isOpen() && this.isBuy() && this.getPrice().compareTo(executionPrice) >= 0;
    }

    public boolean acceptAsk(BigDecimal executionPrice) {
        // order.price = 4, executionProce = 5 -> OK
        return this.isOpen() &&  this.isSell() && this.getPrice().compareTo(executionPrice) <= 0;
    }

    public BigDecimal computeExecutionPrice(BigDecimal executionPrice) {
        return this.isBuy() ? executionPrice : this.getPrice();
    }

    /**
     * Shallow clone all properties apart from id
     * @param otherOrder
     */
    public void update(OrderEntry otherOrder) {
        this.setStatus(otherOrder.getStatus());
        this.setQuantity(otherOrder.getQuantity());
        this.setAvailableQuantity(otherOrder.getAvailableQuantity());
        this.setEntryDate(otherOrder.getEntryDate());
        this.setFinancialInstrumendId(otherOrder.getFinancialInstrumendId());
        this.setPrice(otherOrder.getPrice());
        this.setOrderType(otherOrder.getOrderType());
    }

}
