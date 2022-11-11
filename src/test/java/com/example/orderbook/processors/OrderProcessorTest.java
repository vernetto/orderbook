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
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProcessorTest {

    private static final LocalDateTime TIME_0 = LocalDateTime.of(2022, Month.NOVEMBER, 10, 7, 0);
    private static final LocalDateTime TIME_1 = LocalDateTime.of(2022, Month.NOVEMBER, 10, 9, 0);

    @Test
    void processExecution() {
        OrderProcessor orderProcessor = new OrderProcessor();
        // 2 orders, different price, execution will match the second and partially fill it
        OrderEntry orderEntry1 = new OrderEntry(1L , "ISIN1", BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_0, OrderType.BUY, BigDecimal.valueOf(5), OrderEntryStatus.OPEN);
        OrderEntry orderEntry2 = new OrderEntry(2L , "ISIN1", BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_1, OrderType.BUY, BigDecimal.valueOf(6), OrderEntryStatus.OPEN);
        List<OrderEntry> orders = Arrays.asList(orderEntry1, orderEntry2);
        Execution execution1 =  new Execution(1L, "ISIN1", BigDecimal.valueOf(60), BigDecimal.valueOf(5.5), ExecutionType.OFFER);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        orderProcessor.processExecution(orders, execution1, affectedOrders);
        assertEquals(1, affectedOrders.size());
        OrderEntry affectedOrderEntry1 = affectedOrders.get(0);
        assertEquals(2L, affectedOrderEntry1.getId());
        assertEquals(BigDecimal.valueOf(40), affectedOrderEntry1.getAvailableQuantity());

        // another execution will fill both orders
        Execution execution2 =  new Execution(2L, "ISIN1", BigDecimal.valueOf(200), BigDecimal.valueOf(5), ExecutionType.OFFER);
        List<OrderEntry> affectedOrders2 = new ArrayList<>();
        orderProcessor.processExecution(orders, execution2, affectedOrders2);
        assertEquals(2, affectedOrders2.size());
        OrderEntry affectedOrderEntry2_1 = affectedOrders2.get(0);
        OrderEntry affectedOrderEntry2_2 = affectedOrders2.get(1);
        assertEquals(1L, affectedOrderEntry2_1.getId());
        assertEquals(2L, affectedOrderEntry2_2.getId());
        assertEquals(BigDecimal.ZERO, affectedOrderEntry2_1.getAvailableQuantity());
        assertEquals(BigDecimal.ZERO, affectedOrderEntry2_2.getAvailableQuantity());
        assertEquals(OrderEntryStatus.FILLED, affectedOrderEntry2_1.getStatus());
        assertEquals(OrderEntryStatus.FILLED, affectedOrderEntry2_2.getStatus());

    }
}