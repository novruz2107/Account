package com.novruz.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novruz.account.dto.AccountDTO;
import com.novruz.account.dto.BalanceDTO;
import com.novruz.account.enums.CurrencyType;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createAccount_givenAccountObject_thenReturnsAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCustomerId(1L);
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setCurrency(CurrencyType.USD);
        balanceDTO.setAmount(0);
        List<BalanceDTO> balanceDTOList = new ArrayList<>();
        balanceDTOList.add(balanceDTO);
        accountDTO.setBalanceList(balanceDTOList);

        ResultActions response = mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDTO)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId", is(accountDTO.getCustomerId().intValue())))
                .andExpect(jsonPath("$.balanceList[0].currency", is(CurrencyType.USD.value())));
    }

    @Test
    public void getAccount_givenAccountId_thenReturnsAccount() throws Exception {

        ResultActions response = mockMvc.perform(get("/account")
                .param("accountId", String.valueOf(1)));

        response.andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.balanceList[0].currency", isA(String.class)))
                .andExpect(jsonPath("$.balanceList", isA(List.class)));
    }

}