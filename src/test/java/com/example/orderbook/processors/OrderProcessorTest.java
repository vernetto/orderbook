package com.example.orderbook.processors;

import com.example.orderbook.constants.ExecutionType;
import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.entities.Execution;
import com.example.orderbook.entities.ExecutionHistory;
import com.example.orderbook.entities.OrderEntry;
import com.example.orderbook.mocks.MockObjectFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.orderbook.mocks.MockObjectFactory.ISIN_1;
import static com.example.orderbook.mocks.MockObjectFactory.ISIN_2;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unittests are inspired to the cases in the spreadsheet src/main/resources/static/ordermodel.ods} , where they are much easier to visualize
 *
 */

class OrderProcessorTest {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessorTest.class);

    OrderProcessor orderProcessor = new OrderProcessor();
    MockObjectFactory mockObjectFactory = new  MockObjectFactory();


    @Test
    void processExecutionOfferPartialFill() {
        // 2 orders, different price, execution will match the second and partially fill it
        List<OrderEntry> orders = mockObjectFactory.getOrderEntries();
        Execution execution = mockObjectFactory.getExecutionOffer(ISIN_1, BigDecimal.valueOf(60), BigDecimal.valueOf(5.5));
        List<OrderEntry> affectedOrders = new ArrayList<>();
        List<ExecutionHistory> executionHistoryList = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders, executionHistoryList);
        assertEquals(1, affectedOrders.size());
        OrderEntry affectedOrderEntry1 = affectedOrders.get(0);
        assertEquals(2L, affectedOrderEntry1.getId());
        assertEquals(BigDecimal.valueOf(40), affectedOrderEntry1.getAvailableQuantity());
        assertEquals(OrderEntryStatus.OPEN, affectedOrderEntry1.getStatus());
    }

    @Test
    void processExecutionOfferRejected() {
        List<OrderEntry> orders = mockObjectFactory.getOrderEntries();
        // execution price too high, execution rejected
        Execution execution = mockObjectFactory.getExecutionOffer(ISIN_1, BigDecimal.valueOf(60), BigDecimal.valueOf(7));
        List<OrderEntry> affectedOrders = new ArrayList<>();
        List<ExecutionHistory> executionHistoryList = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders, executionHistoryList);
        assertEquals(0, affectedOrders.size());
    }

    @Test
    void processExecutionOfferFillBoth() {
        List<OrderEntry> orders = mockObjectFactory.getOrderEntries();
        // execution will fill both orders
        Execution execution = mockObjectFactory.getExecutionOffer(ISIN_1, BigDecimal.valueOf(200), BigDecimal.valueOf(5));
        List<OrderEntry> affectedOrders = new ArrayList<>();
        List<ExecutionHistory> executionHistoryList = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders, executionHistoryList);
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
        List<OrderEntry> orders = mockObjectFactory.getOrderEntries();
        // execution price too high, execution rejected
        Execution execution = mockObjectFactory.getExecutionAsk(ISIN_2, BigDecimal.valueOf(60), BigDecimal.valueOf(2));
        List<OrderEntry> affectedOrders = new ArrayList<>();
        List<ExecutionHistory> executionHistoryList = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders, executionHistoryList);
        assertEquals(0, affectedOrders.size());
    }

    @Test
    void processExecutionAskFillBoth() {
        List<OrderEntry> orders = mockObjectFactory.getOrderEntries();
        // execution will fill both orders
        Execution execution = mockObjectFactory.getExecutionAsk(ISIN_2, BigDecimal.valueOf(200), BigDecimal.valueOf(5));
        List<OrderEntry> affectedOrders = new ArrayList<>();
        List<ExecutionHistory> executionHistoryList = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders, executionHistoryList);
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


}