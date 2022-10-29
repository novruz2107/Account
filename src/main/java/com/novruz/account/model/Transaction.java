package com.novruz.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private long transactionId;
    private long accountId;
    private long balanceId;
    private double amount;
    private String currency;
    private String transactionDirection;
    private String description;
}
