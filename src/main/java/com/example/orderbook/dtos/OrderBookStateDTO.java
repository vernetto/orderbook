package com.example.orderbook.dtos;

import java.util.List;

public class OrderBookStateDTO {
    private OrderBookDTO orderBookDTO;
    private List<ExecutionHistoryDTO> executionHistoryDTOList;
    private List<ExecutionDTO> executionDTOList;

    public List<OrderEntryDTO> getOrderEntryDTOList() {
        return orderEntryDTOList;
    }

    public void setOrderEntryDTOList(List<OrderEntryDTO> orderEntryDTOList) {
        this.orderEntryDTOList = orderEntryDTOList;
    }

    private List<OrderEntryDTO> orderEntryDTOList;

    public OrderBookDTO getOrderBookDTO() {
        return orderBookDTO;
    }

    public void setOrderBookDTO(OrderBookDTO orderBookDTO) {
        this.orderBookDTO = orderBookDTO;
    }

    public List<ExecutionHistoryDTO> getExecutionHistoryDTOList() {
        return executionHistoryDTOList;
    }

    public void setExecutionHistoryDTOList(List<ExecutionHistoryDTO> executionHistoryDTOList) {
        this.executionHistoryDTOList = executionHistoryDTOList;
    }

    public List<ExecutionDTO> getExecutionDTOList() {
        return executionDTOList;
    }

    public void setExecutionDTOList(List<ExecutionDTO> executionDTOList) {
        this.executionDTOList = executionDTOList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrderBookStateDTO{");
        sb.append("orderBookDTO=").append(orderBookDTO);
        sb.append(", executionHistoryDTOList=").append(executionHistoryDTOList);
        sb.append(", executionDTOList=").append(executionDTOList);
        sb.append(", orderEntryDTOList=").append(orderEntryDTOList);
        sb.append('}');
        return sb.toString();
    }
}
