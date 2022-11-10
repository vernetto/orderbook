package com.example.orderbook.processors;

import com.example.orderbook.entities.Execution;
import com.example.orderbook.entities.OrderEntry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderProcessor {
    /**
     * Process a single execution against a list of Open Orders having the same financialInstitutionId
     *
     * Execution quantity must be distributed among matching orders in the priority of entry date
     * Execution of type “offer” satisfies order of type “buy” while execution of type “ask” satisfies
     * order of type “sell”.
     * • Order with price lower (buy) than the execution price is not considered.
     * • Order with price higher (sell) than the execution price is not considered.
     * • Orders with financial instrument id not matching the one of the executions is not considered.
     * @param orders
     * @param execution
     * @return a List of affected orders
     */
    public List<OrderEntry> processExecution(List<OrderEntry> orders, Execution execution) {
        return null;
    }
}
