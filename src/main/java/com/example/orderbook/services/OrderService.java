package com.example.orderbook.services;

import com.example.orderbook.constants.ExecutionType;
import com.example.orderbook.constants.OrderBookStatus;
import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.constants.OrderType;
import com.example.orderbook.entities.Execution;
import com.example.orderbook.entities.ExecutionHistory;
import com.example.orderbook.entities.OrderBook;
import com.example.orderbook.entities.OrderEntry;
import com.example.orderbook.exceptions.OrderBookException;
import com.example.orderbook.processors.OrderProcessor;
import com.example.orderbook.repositories.ExecutionHistoryRepository;
import com.example.orderbook.repositories.ExecutionRepository;
import com.example.orderbook.repositories.OrderBookRepository;
import com.example.orderbook.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.orderbook.exceptions.ExceptionCode.*;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final ExecutionRepository executionRepository;
    private final ExecutionHistoryRepository executionHistoryRepository;
    private final OrderProcessor orderProcessor;

    public OrderService(OrderRepository orderRepository, OrderBookRepository orderBookRepository, OrderProcessor orderProcessor, ExecutionRepository executionRepository, ExecutionHistoryRepository executionHistoryRepository) {
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.orderProcessor = orderProcessor;
        this.executionRepository = executionRepository;
        this.executionHistoryRepository = executionHistoryRepository;
    }

    public OrderEntry createOrder(OrderEntry orderEntry) throws OrderBookException {
        logger.info("creating order {} ",  orderEntry);
        OrderBook orderBook = getOrderBook();
        if (orderBook.isClosed()) {
            throw new OrderBookException(ERR_001, "order book is closed, unable to add order");
        }
        orderEntry.setOrderBook(orderBook);
        OrderEntry savedEntry = orderRepository.save(orderEntry);
        logger.info("order successfully saved {}", savedEntry);
        return savedEntry;
    }

    /**
     * The "current" OrderBook will be the one with the highest id, regardless of its status
     */

    public OrderBook getOrderBook() throws OrderBookException {
        return orderBookRepository.findFirstByOrderByIdDesc().orElseThrow(() -> new OrderBookException(ERR_002, "no order book available"));
    }

    public List<ExecutionHistory> processExecution(Execution execution) throws OrderBookException {
        logger.info("processing execution {}", execution);
        OrderBook orderBook = getOrderBook();
        if (!orderBook.isClosed()) {
            throw new OrderBookException(ERR_001, "order book is not closed, unable to process execution");
        }
        execution.setOrderBook(orderBook);

        OrderType orderType = execution.getExecutionType().equals(ExecutionType.OFFER) ? OrderType.BUY : OrderType.SELL;
        List<OrderEntry> orders = orderRepository.findByStatusAndFinancialInstrumendIdAndOrderTypeOrderByEntryDateAsc(OrderEntryStatus.OPEN, execution.getFinancialInstrumendId(), orderType);
        logger.info("these orders are matching the instrument : {}", orders);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        List<ExecutionHistory> executionHistoryList = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders, executionHistoryList);
        executionRepository.save(execution);
        orderRepository.saveAll(affectedOrders);
        executionHistoryList.forEach(executionHistory -> executionHistory.setOrderBook(orderBook));
        executionHistoryRepository.saveAll(executionHistoryList);
        logger.info("these orders have been affected by the execution : {}", affectedOrders);
        return executionHistoryList;
    }


    public void deleteOrder(long id) {
        logger.info("deleting order id={}", id);
        orderRepository.deleteById(id);
        logger.info("deleted order id={}", id);
    }

    public void updateOrder(Long id, OrderEntry orderEntry) throws OrderBookException {
        logger.info("updating order with id {} to value {}", id, orderEntry);
        OrderEntry orderEntryToUpdate = orderRepository.findById(id).orElseThrow(() -> new OrderBookException(ERR_003, "cannot find Order with id " + orderEntry.getId()));
        if (!orderEntryToUpdate.isOpen()) {
            throw new OrderBookException(ERR_005, "order is not open : " + orderEntryToUpdate);
        }
        orderEntryToUpdate.update(orderEntry);
        orderRepository.save(orderEntryToUpdate);
        logger.info("updated order with id {} to value {}", id, orderEntry);
    }


    public void closeOrderBook() throws OrderBookException {
        logger.info("closing orderbook");
        OrderBook orderBook = getOrderBook();
        if (orderBook.isOpen()) {
            orderBook.setStatus(OrderBookStatus.CLOSED);
        }
        else {
            throw new OrderBookException(ERR_002, "Order book is already closed");
        }
        orderBookRepository.save(orderBook);
        logger.info("closed orderbook");
    }

    /**
     * You can open OrderBook only if all Orders are CLOSED, and there is no other OPEN orderbook
     * @throws OrderBookException
     */
    public void openOrderBook() throws OrderBookException {
        logger.info("opening orderbook");
        List<OrderEntry> ordersNotClosed = getOrdersNotClosed();
        if (!ordersNotClosed.isEmpty()) {
            logger.warn("not closed orders : {} ", ordersNotClosed);
            throw new OrderBookException(ERR_004, "unable to open orderbook, there are " + ordersNotClosed.size() + " not closed orders");
        }
        OrderBook orderBook = getOrderBook();
        if (orderBook.isOpen()) {
            throw new OrderBookException(ERR_004, "there is already an open orderbook");
        }
        OrderBook orderBookNew = new OrderBook();
        orderBookNew.setStatus(OrderBookStatus.OPEN);
        orderBookRepository.save(orderBookNew);
        logger.info("opened new orderbook");
    }

    public boolean canCloseOrderBook() {
        List<OrderEntry> ordersNotClosed = getOrdersNotClosed();
        return ordersNotClosed.isEmpty();
    }

    private List<OrderEntry> getOrdersNotClosed() {
        List<OrderEntry> ordersNotClosed = orderRepository.findByStatusNot(OrderEntryStatus.CLOSED);
        return ordersNotClosed;
    }

    public boolean allOrdersCompleted() {
        return getOrdersOpen().isEmpty();
    }

    public List<OrderEntry> getOrdersOpen() {
        List<OrderEntry> ordersOpen = orderRepository.findByStatus(OrderEntryStatus.OPEN);
        return ordersOpen;
    }


    public void closeAllFilledOrders() {
        logger.info("BEGIN closeAllFilledOrders");
        List<OrderEntry> filledOrdersToClose = orderRepository.findByStatus(OrderEntryStatus.FILLED);
        logger.info("closing {} orders", filledOrdersToClose.size());
        filledOrdersToClose.forEach(orderEntry -> orderEntry.setStatus(OrderEntryStatus.CLOSED));
        orderRepository.saveAll(filledOrdersToClose);
        logger.info("END closeAllFilledOrders");
    }

    public List<Execution> getExecutions(OrderBook orderBook) {
        return executionRepository.findByOrderBookId(orderBook.getId());
    }

    public List<OrderEntry> getOrderEntries(OrderBook orderBook) {
        return orderRepository.findByOrderBookId(orderBook.getId());
    }

    public List<ExecutionHistory> getExecutionHistory(OrderBook orderBook) {
        return executionHistoryRepository.findByOrderBookId(orderBook.getId());
    }
}
