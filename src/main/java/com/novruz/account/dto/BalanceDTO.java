package com.novruz.account.dto;

import com.novruz.account.enums.CurrencyType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
    private long balanceId;
    private double amount;
    private CurrencyType currency;
}
