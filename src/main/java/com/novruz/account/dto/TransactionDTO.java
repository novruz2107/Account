package com.novruz.account.dto;

import com.novruz.account.enums.CurrencyType;
import com.novruz.account.enums.TransactionDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private long transactionId;
    private long accountId;
    private double amount;
    private CurrencyType currency;
    private TransactionDirection transactionDirection;
    private String description;
}
