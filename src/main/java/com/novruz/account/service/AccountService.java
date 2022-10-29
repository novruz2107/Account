package com.novruz.account.service;

import com.novruz.account.dto.AccountDTO;
import com.novruz.account.dto.request.CreateTransactionRequest;
import com.novruz.account.dto.response.CreateTransactionResponse;
import com.novruz.account.dto.response.GetTransactionsResponse;

public interface AccountService {
    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO getAccountById(int accountId);

    CreateTransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest);

    GetTransactionsResponse getTransactionsByAccountId(long accountId);
}
