package com.example.orderbook.entities;

import com.example.orderbook.constants.ExecutionType;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@ApiModel(description="Execution of a trade on an OrderEntry")
public class Execution {
    @Id
    @GeneratedValue(generator="execution_seq")
    @SequenceGenerator(name="execution_seq",sequenceName="EXECUTION_SEQ", allocationSize=1, initialValue = 1)
    private Long id;
    @Column(length = 50, nullable = false)
    private String financialInstrumendId;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionType executionType;

    public Execution() {
    }

    public Execution(Long id, String financialInstrumendId, BigDecimal quantity, BigDecimal price, ExecutionType executionType) {
        this.id = id;
        this.financialInstrumendId = financialInstrumendId;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Execution{");
        sb.append("id=").append(id);
        sb.append(", financialInstrumendId='").append(financialInstrumendId).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", price=").append(price);
        sb.append(", executionType=").append(executionType);
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
