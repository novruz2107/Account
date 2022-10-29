package com.novruz.account.dto.response;

import com.novruz.account.dto.TransactionDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTransactionsResponse {
    private List<TransactionDTO> transactions;
}
