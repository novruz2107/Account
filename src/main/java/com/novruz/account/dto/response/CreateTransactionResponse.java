package com.novruz.account.dto.response;

import com.novruz.account.dto.BalanceDTO;
import com.novruz.account.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionResponse {
    private TransactionDTO transaction;
    private BalanceDTO balance;
}
