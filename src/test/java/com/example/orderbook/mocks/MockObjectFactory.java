package com.example.orderbook.mocks;

import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.constants.OrderType;
import com.example.orderbook.entities.OrderEntry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MockObjectFactory {
    public static final Date TIME_1 = Date.from(LocalDateTime.of(2022, Month.NOVEMBER, 10, 7, 0).atZone(ZoneId.systemDefault()).toInstant());
    public static final Date TIME_2 = Date.from(LocalDateTime.of(2022, Month.NOVEMBER, 10, 10, 0).atZone(ZoneId.systemDefault()).toInstant());
    public static final String ISIN_1 = "ISIN1";
    public static final String ISIN_2 = "ISIN2";

    public List<OrderEntry> getOrderEntries() {
        OrderEntry orderEntry1 = new OrderEntry(1L, ISIN_1, BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_1, OrderType.BUY, BigDecimal.valueOf(5), OrderEntryStatus.OPEN);
        OrderEntry orderEntry2 = new OrderEntry(2L, ISIN_1, BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_2, OrderType.BUY, BigDecimal.valueOf(6), OrderEntryStatus.OPEN);
        OrderEntry orderEntry3 = new OrderEntry(3L, ISIN_2, BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_2, OrderType.BUY, BigDecimal.valueOf(6), OrderEntryStatus.OPEN);
        OrderEntry orderEntry4 = new OrderEntry(4L, ISIN_2, BigDecimal.valueOf(50), BigDecimal.valueOf(50), TIME_1, OrderType.SELL, BigDecimal.valueOf(3), OrderEntryStatus.OPEN);
        OrderEntry orderEntry5 = new OrderEntry(5L, ISIN_2, BigDecimal.valueOf(50), BigDecimal.valueOf(50), TIME_2, OrderType.SELL, BigDecimal.valueOf(4), OrderEntryStatus.OPEN);
        List<OrderEntry> orders = Arrays.asList(orderEntry1, orderEntry2, orderEntry3, orderEntry4, orderEntry5);
        return orders;
    }
}
