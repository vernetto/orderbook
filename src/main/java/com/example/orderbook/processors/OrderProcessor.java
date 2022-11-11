package com.example.orderbook.processors;

import com.example.orderbook.constants.OrderEntryStatus;
import com.example.orderbook.entities.Execution;
import com.example.orderbook.entities.ExecutionHistory;
import com.example.orderbook.entities.OrderEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

    /**
     * Process a single execution against a list of Open Orders having the same financialInstitutionId
     * <p>
     * Execution quantity must be distributed among matching ordersToProcess in the priority of entry date
     * Execution of type “offer” satisfies order of type “buy” while execution of type “ask” satisfies
     * order of type “sell”.
     * • Order with price lower (buy) than the execution price is not considered.
     * • Order with price higher (sell) than the execution price is not considered.
     * • Orders with financial instrument id not matching the one of the executions is not considered.
     *
     * @param ordersToProcess
     * @param execution
     * @param executionHistoryList
     * @return a List of affected ordersToProcess
     */
    public void processExecution(List<OrderEntry> ordersToProcess, Execution execution, List<OrderEntry> affectedOrders, List<ExecutionHistory> executionHistoryList) {
        // only ISINs matching the execution ISIN should be considered, and ordertype should match execution type, and order accepts execution price
        List<OrderEntry> filteredOrdersToProcess = ordersToProcess.stream().filter(
                orderEntry -> (
                        orderEntry.getFinancialInstrumendId().equals(execution.getFinancialInstrumendId()) &&
                                (execution.isAsk() ? orderEntry.isSell() : orderEntry.isBuy()) &&
                                (execution.isOffer() ? orderEntry.acceptOffer(execution.getPrice()) : orderEntry.acceptAsk(execution.getPrice()))
                )
        ).sorted((o1, o2) -> o1.getEntryDate().compareTo(o2.getEntryDate())).collect(Collectors.toList());

        BigDecimal totalQuantityToAllocate = execution.getQuantity();
        for (OrderEntry orderEntry : filteredOrdersToProcess) {
            BigDecimal quantityToAllocate = orderEntry.getAvailableQuantity().min(totalQuantityToAllocate);
            totalQuantityToAllocate = totalQuantityToAllocate.subtract(quantityToAllocate);
            orderEntry.setAvailableQuantity(orderEntry.getAvailableQuantity().subtract(quantityToAllocate));
            if (orderEntry.getAvailableQuantity().compareTo(BigDecimal.ZERO) == 0) {
                orderEntry.setStatus(OrderEntryStatus.FILLED);
            }
            BigDecimal price = orderEntry.fillPrice(execution.getPrice());
            executionHistoryList.add(new ExecutionHistory(execution, orderEntry, quantityToAllocate, price));
            affectedOrders.add(orderEntry);
            if (totalQuantityToAllocate.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }

    }
}
