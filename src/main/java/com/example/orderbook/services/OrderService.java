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

    final private OrderRepository orderRepository;
    final private OrderBookRepository orderBookRepository;
    final private ExecutionRepository executionRepository;
    final private ExecutionHistoryRepository executionHistoryRepository;
    final private OrderProcessor orderProcessor;

    public OrderService(OrderRepository orderRepository, OrderBookRepository orderBookRepository, OrderProcessor orderProcessor, ExecutionRepository executionRepository, ExecutionHistoryRepository executionHistoryRepository) {
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.orderProcessor = orderProcessor;
        this.executionRepository = executionRepository;
        this.executionHistoryRepository = executionHistoryRepository;
    }

    public OrderEntry createOrder(OrderEntry orderEntry) throws OrderBookException {
        logger.info("creating order " + orderEntry);
        OrderBook orderBook = getOrderBook();
        if (orderBook.isClosed()) {
            throw new OrderBookException(ERR_001, "order book is closed, unable to add order");
        }
        orderEntry.setOrderBook(orderBook);
        orderRepository.save(orderEntry);
        logger.info("order successfully saved " + orderEntry);
        return orderEntry;
    }

    private OrderBook getOrderBook() throws OrderBookException {
        return orderBookRepository.findById(1L).orElseThrow(() -> new OrderBookException(ERR_002, "no order book available"));
    }

    public List<OrderEntry> processExecution(Execution execution) throws OrderBookException {
        logger.info("processing execution " + execution);
        OrderBook orderBook = getOrderBook();
        if (!orderBook.isClosed()) {
            throw new OrderBookException(ERR_001, "order book is not closed, unable to process execution");
        }
        execution.setOrderBook(orderBook);

        OrderType orderType = execution.getExecutionType().equals(ExecutionType.OFFER) ? OrderType.BUY : OrderType.SELL;
        List<OrderEntry> orders = orderRepository.findByStatusAndFinancialInstrumendIdAndOrderTypeOrderByEntryDateAsc(OrderEntryStatus.OPEN, execution.getFinancialInstrumendId(), orderType);
        logger.info("these orders are matching the instrument : " + orders);
        List<OrderEntry> affectedOrders = new ArrayList<>();
        List<ExecutionHistory> executionHistoryList = new ArrayList<>();
        orderProcessor.processExecution(orders, execution, affectedOrders, executionHistoryList);
        executionRepository.save(execution);
        orderRepository.saveAll(affectedOrders);
        executionHistoryRepository.saveAll(executionHistoryList);
        logger.info("these orders have been affected by the execution : " + affectedOrders);
        return affectedOrders;
    }


    public void deleteOrder(long id) {
        logger.info("deleting order id=" + id);
        orderRepository.deleteById(id);
        logger.info("deleted order id=" + id);
    }

    public void updateOrder(OrderEntry orderEntry) throws OrderBookException {
        logger.info("updating order " + orderEntry);
        OrderEntry orderEntryToUpdate = orderRepository.findById(orderEntry.getId()).orElseThrow(() -> new OrderBookException(ERR_003, "cannot find Order with id " + orderEntry.getId()));
        if (!orderEntryToUpdate.isOpen()) {
            throw new OrderBookException(ERR_005, "order is not open : " + orderEntryToUpdate);
        }
        orderEntryToUpdate.update(orderEntry);
        orderRepository.save(orderEntryToUpdate);
        logger.info("updated order " + orderEntry);
    }


    public void closeOrderBook() throws OrderBookException {
        logger.info("closing orderbook");
        OrderBook orderBook = getOrderBook();
        orderBook.setStatus(OrderBookStatus.CLOSED);
        orderBookRepository.save(orderBook);
        logger.info("closed orderbook");
    }

    /**
     * You can open OrderBook only if all Orders are CLOSED
     * @throws OrderBookException
     */
    public void openOrderBook() throws OrderBookException {
        logger.info("opening orderbook");
        List<OrderEntry> ordersNotClosed = getOrdersNotClosed();
        if (!ordersNotClosed.isEmpty()) {
            logger.warn("not closed orders : " + ordersNotClosed);
            throw new OrderBookException(ERR_004, "unable to open orderbook, there are " + ordersNotClosed.size() + " not closed orders");
        }
        OrderBook orderBook = getOrderBook();
        orderBook.setStatus(OrderBookStatus.OPEN);
        orderBookRepository.save(orderBook);
        logger.info("opened orderbook");
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


    public void generateExecutionReport() {
        logger.info("BEGIN generateExecutionReport");
        // TODO
        logger.info("END generateExecutionReport");
    }

    public void closeAllFilledOrders() {
        logger.info("BEGIN closeAllFilledOrders");
        // TODO
        logger.info("END closeAllFilledOrders");
    }
}
