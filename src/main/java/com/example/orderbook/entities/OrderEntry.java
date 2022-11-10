package com.example.orderbook.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class OrderEntry {
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
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    @Column
    private BigDecimal price;


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


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Order{");
        sb.append("id=").append(id);
        sb.append(", financialInstrumendId='").append(financialInstrumendId).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", entryDate=").append(entryDate);
        sb.append(", orderType=").append(orderType);
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntry orderEntry = (OrderEntry) o;
        return id.equals(orderEntry.id) && financialInstrumendId.equals(orderEntry.financialInstrumendId) && quantity.equals(orderEntry.quantity) && entryDate.equals(orderEntry.entryDate) && orderType == orderEntry.orderType && price.equals(orderEntry.price) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, financialInstrumendId, quantity, entryDate, orderType, price);
    }
}
