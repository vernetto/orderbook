package com.example.orderbook.dtos;

import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.constants.OrderType;
import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@ApiModel(description="Order to be filled by executions")
public class OrderEntryDTO {
    private Long id;
    private String financialInstrumendId;
    private BigDecimal quantity;
    private BigDecimal availableQuantity;
    private Date entryDate;
    private OrderType orderType;
    private BigDecimal price;
    private OrderEntryStatus status;

    OrderBookDTO orderBookDTO;

    public OrderEntryDTO(Long id, String financialInstrumendId, BigDecimal quantity, BigDecimal availableQuantity, Date entryDate, OrderType orderType, BigDecimal price, OrderEntryStatus status) {
        this.id = id;
        this.financialInstrumendId = financialInstrumendId;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.entryDate = entryDate;
        this.orderType = orderType;
        this.price = price;
        this.status = status;
    }

    public OrderEntryDTO() {

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

    public OrderBookDTO getOrderBookDTO() {
        return orderBookDTO;
    }

    public void setOrderBookDTO(OrderBookDTO orderBookDTO) {
        this.orderBookDTO = orderBookDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntryDTO that = (OrderEntryDTO) o;
        return id.equals(that.id) && financialInstrumendId.equals(that.financialInstrumendId) && quantity.equals(that.quantity) && availableQuantity.equals(that.availableQuantity) && entryDate.equals(that.entryDate) && orderType == that.orderType && price.equals(that.price) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, financialInstrumendId, quantity, availableQuantity, entryDate, orderType, price, status);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderEntry{");
        sb.append("id=").append(id);
        sb.append(", financialInstrumendId='").append(financialInstrumendId).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", availableQuantity=").append(availableQuantity);
        sb.append(", entryDate=").append(entryDate);
        sb.append(", orderType=").append(orderType);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append(", orderBookId=").append(orderBookDTO != null ? orderBookDTO.getId() : "null");
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


    /**
     * Shallow clone all properties apart from id
     * @param otherOrder
     */
    public void update(OrderEntryDTO otherOrder) {
        this.setStatus(otherOrder.getStatus());
        this.setQuantity(otherOrder.getQuantity());
        this.setAvailableQuantity(otherOrder.getAvailableQuantity());
        this.setEntryDate(otherOrder.getEntryDate());
        this.setFinancialInstrumendId(otherOrder.getFinancialInstrumendId());
        this.setPrice(otherOrder.getPrice());
        this.setOrderType(otherOrder.getOrderType());
    }

}
