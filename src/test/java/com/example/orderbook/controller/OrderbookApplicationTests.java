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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderbookApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(OrderbookApplicationTests.class);

    MockObjectFactory mockObjectFactory = new  MockObjectFactory();
    ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    //@Autowired
    //private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createOrdersAndSubmitExecutions() throws Exception {
        logger.info("BEGIN creating orders");
        mockObjectFactory.getOrderEntries().forEach(orderEntry -> {
            try {
                String postBody = objectMapper.writeValueAsString(orderEntry);
                this.mockMvc.perform(post("/createOrder").content(postBody).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        logger.info("END creating orders");

        Execution execution = mockObjectFactory.getExecutionOffer(ISIN_1, BigDecimal.valueOf(60), BigDecimal.valueOf(5.5));
        String postBody = objectMapper.writeValueAsString(execution);
        // orderbook is open -> error
        this.mockMvc.perform(post("/processExecution").content(postBody).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is5xxServerError());



    }
}


