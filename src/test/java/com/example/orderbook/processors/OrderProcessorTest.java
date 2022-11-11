package com.example.orderbook.processors;

import com.example.orderbook.constants.ExecutionType;
import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.constants.OrderType;
import com.example.orderbook.entities.Execution;
import com.example.orderbook.entities.OrderEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderProcessorTest {

    private static final LocalDateTime TIME_0 = LocalDateTime.of(2022, Month.NOVEMBER, 10, 7, 0);
    private static final LocalDateTime TIME_1 = LocalDateTime.of(2022, Month.NOVEMBER, 10, 9, 0);

    @Test
    void processExecution() {
        OrderProcessor orderProcessor = new OrderProcessor();
        OrderEntry orderEntry1 = new OrderEntry(1L , "ISIN1", BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_0, OrderType.BUY, BigDecimal.valueOf(5), OrderEntryStatus.OPEN);
        OrderEntry orderEntry2 = new OrderEntry(2L , "ISIN1", BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_1, OrderType.BUY, BigDecimal.valueOf(6), OrderEntryStatus.OPEN);
        List<OrderEntry> orders = Arrays.asList(orderEntry1, orderEntry2);
        Execution execution =  new Execution(1L, "ISIN1", BigDecimal.valueOf(60), BigDecimal.valueOf(5.5), ExecutionType.OFFER);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders);
        assertNotNull(affectedOrders);
        assertEquals(1, affectedOrders.size());
        OrderEntry orderEntry = affectedOrders.get(0);
        assertEquals(2L, orderEntry.getId());
        assertEquals(BigDecimal.valueOf(40), orderEntry.getAvailableQuantity());

    }
}