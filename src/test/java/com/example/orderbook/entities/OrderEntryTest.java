package com.example.orderbook.entities;

import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.constants.OrderType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderEntryTest {

    private static final LocalDateTime TIME_0 = LocalDateTime.of(2022, Month.NOVEMBER, 10, 7, 0);
    private static final LocalDateTime TIME_1 = LocalDateTime.of(2022, Month.NOVEMBER, 10, 9, 0);


    @Test
    void acceptAsk() {
        OrderEntry orderEntryBuy1 = new OrderEntry(1L , "ISIN1", BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_0, OrderType.BUY, BigDecimal.valueOf(5), OrderEntryStatus.OPEN);
        OrderEntry orderEntrySell1 = new OrderEntry(2L , "ISIN1", BigDecimal.valueOf(100), BigDecimal.valueOf(100), TIME_1, OrderType.SELL, BigDecimal.valueOf(5), OrderEntryStatus.OPEN);

        // Order with price lower (buy) than the execution price is not considered.
        assertFalse(orderEntryBuy1.acceptOffer(BigDecimal.valueOf(5.5)));
        assertTrue(orderEntryBuy1.acceptOffer(BigDecimal.valueOf(5)));
        assertTrue(orderEntryBuy1.acceptOffer(BigDecimal.valueOf(4)));
        // test wrong ordertype
        assertFalse(orderEntryBuy1.acceptAsk(BigDecimal.valueOf(5.5)));
        assertFalse(orderEntryBuy1.acceptAsk(BigDecimal.valueOf(4)));

        // Order with price higher (sell) than the execution price is not considered.
        assertTrue(orderEntrySell1.acceptAsk(BigDecimal.valueOf(5.5)));
        assertTrue(orderEntrySell1.acceptAsk(BigDecimal.valueOf(5)));
        assertFalse(orderEntrySell1.acceptAsk(BigDecimal.valueOf(4)));
        // test wrong ordertype
        assertFalse(orderEntrySell1.acceptOffer(BigDecimal.valueOf(5.5)));
        assertFalse(orderEntrySell1.acceptOffer(BigDecimal.valueOf(5)));

    }


}