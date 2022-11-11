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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unittests are inspired to the cases in the spreadsheet src/main/resources/static/ordermodel.ods} , where they are much easier to visualize
 *
 */

class OrderProcessorTest {

    private static final LocalDateTime TIME_1 = LocalDateTime.of(2022, Month.NOVEMBER, 10, 7, 0);
    private static final LocalDateTime TIME_2 = LocalDateTime.of(2022, Month.NOVEMBER, 10, 9, 0);
    public static final String ISIN_1 = "ISIN1";
    public static final String ISIN_2 = "ISIN2";

    OrderProcessor orderProcessor = new OrderProcessor();

    @Test
    void processExecutionOfferPartialFill() {
        // 2 orders, different price, execution will match the second and partially fill it
        List<OrderEntry> orders = getOrderEntries();
        Execution execution = new Execution(1L, ISIN_1, BigDecimal.valueOf(60), BigDecimal.valueOf(5.5), ExecutionType.OFFER);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders);
        assertEquals(1, affectedOrders.size());
        OrderEntry affectedOrderEntry1 = affectedOrders.get(0);
        assertEquals(2L, affectedOrderEntry1.getId());
        assertEquals(BigDecimal.valueOf(40), affectedOrderEntry1.getAvailableQuantity());
        assertEquals(OrderEntryStatus.OPEN, affectedOrderEntry1.getStatus());
    }

    @Test
    void processExecutionOfferRejected() {
        List<OrderEntry> orders = getOrderEntries();
        // execution price too high, execution rejected
        Execution execution = new Execution(1L, ISIN_1, BigDecimal.valueOf(60), BigDecimal.valueOf(7), ExecutionType.OFFER);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders);
        assertEquals(0, affectedOrders.size());
    }

    @Test
    void processExecutionOfferFillBoth() {
        List<OrderEntry> orders = getOrderEntries();
        // execution will fill both orders
        Execution execution = new Execution(1L, ISIN_1, BigDecimal.valueOf(200), BigDecimal.valueOf(5), ExecutionType.OFFER);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders);
        assertEquals(2, affectedOrders.size());
        OrderEntry affectedOrderEntry2_1 = affectedOrders.get(0);
        OrderEntry affectedOrderEntry2_2 = affectedOrders.get(1);
        assertEquals(1L, affectedOrderEntry2_1.getId());
        assertEquals(2L, affectedOrderEntry2_2.getId());
        assertEquals(BigDecimal.ZERO, affectedOrderEntry2_1.getAvailableQuantity());
        assertEquals(BigDecimal.ZERO, affectedOrderEntry2_2.getAvailableQuantity());
        assertEquals(OrderEntryStatus.FILLED, affectedOrderEntry2_1.getStatus());
        assertEquals(OrderEntryStatus.FILLED, affectedOrderEntry2_2.getStatus());
    }

    @Test
    void processExecutionAskRejected() {
        List<OrderEntry> orders = getOrderEntries();
        // execution price too high, execution rejected
        Execution execution = new Execution(1L, ISIN_2, BigDecimal.valueOf(60), BigDecimal.valueOf(2), ExecutionType.ASK);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders);
        assertEquals(0, affectedOrders.size());
    }

    @Test
    void processExecutionAskFillBoth() {
        List<OrderEntry> orders = getOrderEntries();
        // execution will fill both orders
        Execution execution = new Execution(1L, ISIN_2, BigDecimal.valueOf(200), BigDecimal.valueOf(5), ExecutionType.ASK);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders);
        assertEquals(2, affectedOrders.size());
        OrderEntry affectedOrderEntry2_1 = affectedOrders.get(0);
        OrderEntry affectedOrderEntry2_2 = affectedOrders.get(1);
        assertEquals(4L, affectedOrderEntry2_1.getId());
        assertEquals(5L, affectedOrderEntry2_2.getId());
        assertEquals(BigDecimal.ZERO, affectedOrderEntry2_1.getAvailableQuantity());
        assertEquals(BigDecimal.ZERO, affectedOrderEntry2_2.getAvailableQuantity());
        assertEquals(OrderEntryStatus.FILLED, affectedOrderEntry2_1.getStatus());
        assertEquals(OrderEntryStatus.FILLED, affectedOrderEntry2_2.getStatus());
    }



    private static List<OrderEntry> getOrderEntries() {
        OrderEntry orderEntry1 = new OrderEntry(1L, ISIN_1, BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_1, OrderType.BUY, BigDecimal.valueOf(5), OrderEntryStatus.OPEN);
        OrderEntry orderEntry2 = new OrderEntry(2L, ISIN_1, BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_2, OrderType.BUY, BigDecimal.valueOf(6), OrderEntryStatus.OPEN);
        OrderEntry orderEntry3 = new OrderEntry(3L, ISIN_2, BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_2, OrderType.BUY, BigDecimal.valueOf(6), OrderEntryStatus.OPEN);
        OrderEntry orderEntry4 = new OrderEntry(4L, ISIN_2, BigDecimal.valueOf(50), BigDecimal.valueOf(50), TIME_1, OrderType.SELL, BigDecimal.valueOf(3), OrderEntryStatus.OPEN);
        OrderEntry orderEntry5 = new OrderEntry(5L, ISIN_2, BigDecimal.valueOf(50), BigDecimal.valueOf(50), TIME_2, OrderType.SELL, BigDecimal.valueOf(4), OrderEntryStatus.OPEN);
        List<OrderEntry> orders = Arrays.asList(orderEntry1, orderEntry2, orderEntry3, orderEntry4, orderEntry5);
        return orders;
    }


}