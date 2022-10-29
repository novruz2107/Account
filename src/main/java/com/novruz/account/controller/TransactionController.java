package com.novruz.account.controller;

import com.novruz.account.dto.request.CreateTransactionRequest;
import com.novruz.account.dto.response.CreateTransactionResponse;
import com.novruz.account.dto.response.GetTransactionsResponse;
import com.novruz.account.service.AccountService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("transaction")
public class TransactionController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<CreateTransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest createTransactionRequest) {
        var result = accountService.createTransaction(createTransactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<GetTransactionsResponse> getTransactions(@RequestParam long accountId) {
        var result = accountService.getTransactionsByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.FOUND).body(result);
    }
}
