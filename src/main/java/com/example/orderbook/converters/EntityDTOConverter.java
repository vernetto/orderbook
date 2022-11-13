package com.example.orderbook.converters;

import com.example.orderbook.dtos.ExecutionDTO;
import com.example.orderbook.dtos.ExecutionHistoryDTO;
import com.example.orderbook.dtos.OrderBookDTO;
import com.example.orderbook.dtos.OrderEntryDTO;
import com.example.orderbook.entities.Execution;
import com.example.orderbook.entities.ExecutionHistory;
import com.example.orderbook.entities.OrderBook;
import com.example.orderbook.entities.OrderEntry;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityDTOConverter {
    private final ModelMapper modelMapper;

    public EntityDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public OrderEntry convertOrderEntryDTOToEntity(OrderEntryDTO orderEntryDTO) {
        OrderEntry orderEntry = modelMapper.map(orderEntryDTO, OrderEntry.class);
        return orderEntry;
    }

    public OrderEntryDTO convertOrderEntryEntityToDTO(OrderEntry orderEntry) {
        OrderEntryDTO orderEntryDTO = modelMapper.map(orderEntry, OrderEntryDTO.class);
        return orderEntryDTO;
    }

    public Execution convertExecutionDTOToEntity(ExecutionDTO executionDTO) {
        Execution execution = modelMapper.map(executionDTO, Execution.class);
        return execution;
    }

    public List<ExecutionHistoryDTO> convertExecutionHistoryEntityToDTOList(List<ExecutionHistory> executionHistory) {
        List<ExecutionHistoryDTO> executionHistoryDTOList = modelMapper.map(executionHistory, new TypeToken<List<ExecutionHistoryDTO>>() {}.getType());
        return executionHistoryDTOList;
    }

    public OrderBookDTO convertOrderBookEntityToDTO(OrderBook orderBook){
        return modelMapper.map(orderBook, OrderBookDTO.class);
    }

    public List<ExecutionDTO> convertExecutionListEntityToDTO(List<Execution> executionList) {
        List<ExecutionDTO> executionDTOList = modelMapper.map(executionList, new TypeToken<List<ExecutionDTO>>() {}.getType());
        return executionDTOList;
    }

    public List<OrderEntryDTO> convertOrderBookListEntityToDTO(List<OrderEntry> orderEntryList) {
        List<OrderEntryDTO> orderEntryDTOList = modelMapper.map(orderEntryList, new TypeToken<List<OrderEntryDTO>>() {}.getType());
        return orderEntryDTOList;
    }
}
