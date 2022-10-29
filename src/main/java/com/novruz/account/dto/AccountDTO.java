package com.novruz.account.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private long accountId;

    @NotNull(message = "customerId must be provided")
    private Long customerId;

    @NotNull
    private List<BalanceDTO> balanceList;
}
