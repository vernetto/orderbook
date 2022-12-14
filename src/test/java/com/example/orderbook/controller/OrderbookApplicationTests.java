package com.example.orderbook.controller;

import com.example.orderbook.entities.Execution;
import com.example.orderbook.mocks.MockObjectFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.example.orderbook.mocks.MockObjectFactory.ISIN_1;
import static com.example.orderbook.mocks.MockObjectFactory.ISIN_2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderbookApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(OrderbookApplicationTests.class);

    MockObjectFactory mockObjectFactory = new MockObjectFactory();
    ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createOrdersAndSubmitExecutions() throws Exception {
        logger.info("BEGIN creating orders");
        mockObjectFactory.getOrderEntries().forEach(orderEntry -> {
            try {
                String postBody = objectMapper.writeValueAsString(orderEntry);
                this.mockMvc.perform(post("/order/v1/createOrder")
                                .content(postBody)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        logger.info("END creating orders");

        Execution execution = mockObjectFactory.getExecutionOffer(ISIN_1, BigDecimal.valueOf(50), BigDecimal.valueOf(5.5));
        String postBody = objectMapper.writeValueAsString(execution);
        logger.info("orderbook is open -> expecting error");
        this.mockMvc.perform(post("/order/v1/processExecution")
                        .content(postBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());

        logger.info("close orderbook");
        this.mockMvc.perform(post("/order/v1/closeOrderBook").content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


        logger.info("create order with orderbook closed, should fail");
        postBody = objectMapper.writeValueAsString(mockObjectFactory.getOrderEntries().get(0));
        this.mockMvc.perform(post("/order/v1/createOrder")
                        .content(postBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());


        logger.info("BEGIN process execution, should be OK");
        postBody = objectMapper.writeValueAsString(execution);
        this.mockMvc.perform(post("/order/v1/processExecution")
                        .content(postBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$..filledQuantity").value(50));
        logger.info("END process execution, should be OK");

        execution = mockObjectFactory.getExecutionOffer(ISIN_1, BigDecimal.valueOf(80), BigDecimal.valueOf(8));
        postBody = objectMapper.writeValueAsString(execution);
        logger.info("BEGIN process execution, should be OK but no fill");
        this.mockMvc.perform(post("/order/v1/processExecution")
                        .content(postBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$..filledQuantity").doesNotExist());

        logger.info("END process execution, should be OK but no fill");


        execution = mockObjectFactory.getExecutionOffer(ISIN_1, BigDecimal.valueOf(200), BigDecimal.valueOf(4));
        postBody = objectMapper.writeValueAsString(execution);
        logger.info("BEGIN process execution, should be OK with 2 orders filled");
        this.mockMvc.perform(post("/order/v1/processExecution")
                        .content(postBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$[0]['filledQuantity']").value(100))
                .andExpect(jsonPath("$[1]['filledQuantity']").value(50));
        logger.info("END process execution, should be OK with 2 orders filled");

        execution = mockObjectFactory.getExecutionOffer(ISIN_2, BigDecimal.valueOf(200), BigDecimal.valueOf(4));
        postBody = objectMapper.writeValueAsString(execution);
        logger.info("BEGIN process execution, should be OK with 1 order filled");
        this.mockMvc.perform(post("/order/v1/processExecution")
                        .content(postBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$[0]['filledQuantity']").value(100));
        logger.info("END process execution, should be OK with 1 order filled");

        execution = mockObjectFactory.getExecutionAsk(ISIN_2, BigDecimal.valueOf(200), BigDecimal.valueOf(2));
        postBody = objectMapper.writeValueAsString(execution);
        logger.info("BEGIN process execution, should be OK with 0 orders filled");
        this.mockMvc.perform(post("/order/v1/processExecution")
                        .content(postBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$..filledQuantity").doesNotExist());
        logger.info("END process execution, should be OK with 0 orders filled");

        execution = mockObjectFactory.getExecutionAsk(ISIN_2, BigDecimal.valueOf(200), BigDecimal.valueOf(8));
        postBody = objectMapper.writeValueAsString(execution);
        logger.info("BEGIN process execution, should be OK with 2 orders filled");
        this.mockMvc.perform(post("/order/v1/processExecution")
                        .content(postBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$[0]['filledQuantity']").value(50))
                .andExpect(jsonPath("$[1]['filledQuantity']").value(50));
        logger.info("END process execution, should be OK with 2 orders filled");


        logger.info("open orderbook");
        this.mockMvc.perform(post("/order/v1/openOrderBook").content(postBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }
}


