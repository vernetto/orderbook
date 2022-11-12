package com.example.orderbook.controller;

import com.example.orderbook.mocks.MockObjectFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderbookApplicationTests {

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
        //assertTrue(this.restTemplate.postForObject("http://localhost:" + port + "/createOrder", String.class).contains("Hello, World"));
        String post1 = """
                {
                  "availableQuantity": 100,
                  "entryDate": "2022-11-11T10:42:23.740Z",
                  "financialInstrumendId": "ISIN1",
                  "id": 0,
                  "orderType": "BUY",
                  "price": 5,
                  "quantity": 100,
                  "status": "OPEN"
                }
                 """;
        mockObjectFactory.getOrderEntries().forEach(orderEntry -> {
            try {
                String postBody = objectMapper.writeValueAsString(orderEntry);
                this.mockMvc.perform(post("/createOrder").content(postBody).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("done");
    }
}


