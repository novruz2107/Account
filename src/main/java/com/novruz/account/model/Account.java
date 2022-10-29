package com.novruz.account.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private int accountId;
    private int customerId;
    private List<Balance> balanceList;
}
