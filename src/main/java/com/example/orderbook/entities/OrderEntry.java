package com.example.orderbook.entities;

import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.constants.OrderType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
public class OrderEntry {
    @Id
    @GeneratedValue(generator="orderentry_seq")
    @SequenceGenerator(name="orderentry_seq",sequenceName="ORDERENTRY_SEQ", allocationSize=1, initialValue = 1)
    private Long id;
    @Column(length = 50, nullable = false)
    private String financialInstrumentId;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Column(nullable = false)
    private BigDecimal availableQuantity;
    @Column(nullable = false)
    private Date entryDate;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderEntryStatus status;
    @ManyToOne
    OrderBook orderBook;

    public OrderEntry(Long id, String financialInstrumentId, BigDecimal quantity, BigDecimal availableQuantity, Date entryDate, OrderType orderType, BigDecimal price, OrderEntryStatus status) {
        this.id = id;
        this.financialInstrumentId = financialInstrumentId;
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

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
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

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntry that = (OrderEntry) o;
        return id.equals(that.id) && financialInstrumentId.equals(that.financialInstrumentId) && quantity.equals(that.quantity) && availableQuantity.equals(that.availableQuantity) && entryDate.equals(that.entryDate) && orderType == that.orderType && price.equals(that.price) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, financialInstrumentId, quantity, availableQuantity, entryDate, orderType, price, status);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderEntry{");
        sb.append("id=").append(id);
        sb.append(", financialInstrumentId='").append(financialInstrumentId).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", availableQuantity=").append(availableQuantity);
        sb.append(", entryDate=").append(entryDate);
        sb.append(", orderType=").append(orderType);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append(", orderBookId=").append(orderBook != null ? orderBook.getId() : "null");
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

    /**
     * Returns the buy/sell price at which an order is filled given an executionPrice
     */
    public BigDecimal fillPrice(BigDecimal executionPrice) {
        return this.isBuy() ? executionPrice : this.getPrice();
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
        this.setFinancialInstrumentId(otherOrder.getFinancialInstrumentId());
        this.setPrice(otherOrder.getPrice());
        this.setOrderType(otherOrder.getOrderType());
    }

}
