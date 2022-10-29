package com.novruz.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novruz.account.dto.AccountDTO;
import com.novruz.account.dto.BalanceDTO;
import com.novruz.account.dto.request.CreateTransactionRequest;
import com.novruz.account.enums.CurrencyType;
import com.novruz.account.enums.TransactionDirection;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTransaction_givenRequestObject_thenReturnsTransaction() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setDescription("description");
        request.setTransactionDirection(TransactionDirection.IN);
        request.setAccountId(1);
        request.setCurrency(CurrencyType.USD);
        request.setAmount(100);

        ResultActions response = mockMvc.perform(post("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transaction.amount", is(request.getAmount())))
                .andExpect(jsonPath("$.transaction.currency", is(CurrencyType.USD.value())));
    }

    @Test
    public void getTransaction_givenAccountId_thenReturnsTransaction() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setDescription("description");
        request.setTransactionDirection(TransactionDirection.IN);
        request.setAccountId(1);
        request.setCurrency(CurrencyType.USD);
        request.setAmount(100);

        ResultActions response = mockMvc.perform(get("/transaction").param("accountId", String.valueOf(1)));

        response.andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.transactions[0].accountId", is(1)))
                .andExpect(jsonPath("$.transactions", isA(List.class)));
    }
}