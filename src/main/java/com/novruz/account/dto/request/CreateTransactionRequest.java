package com.novruz.account.dto.request;

import com.novruz.account.enums.CurrencyType;
import com.novruz.account.enums.TransactionDirection;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {
    @NotNull(message = "Account missing")
    private long accountId;

    @DecimalMin(value = "0")
    private double amount;
    private CurrencyType currency;
    private TransactionDirection transactionDirection;

    @NotBlank(message = "Description missing")
    private String description;
}
