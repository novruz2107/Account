package com.novruz.account.model;

import com.novruz.account.enums.TransactionDirection;
import com.novruz.account.exception.OperationInvalidException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    private long balanceId;
    private long accountId;
    private double amount;
    private String currency;
    private List<Transaction> transactions;

    public void setAmount(double amount) {
        if (amount < 0) {
            throw new OperationInvalidException("Insufficient funds");
        }
        this.amount = amount;
    }

    public void applyTransaction(double transAmount, TransactionDirection direction) {
        if (direction.equals(TransactionDirection.OUT)) {
            setAmount(amount - transAmount);
        } else {
            setAmount(amount + transAmount);
        }
    }
}
