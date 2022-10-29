package com.novruz.account.mapper;

import com.novruz.account.dto.TransactionDTO;
import com.novruz.account.dto.request.CreateTransactionRequest;
import com.novruz.account.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction transactionRequestToTransaction(CreateTransactionRequest createTransactionRequest);

    TransactionDTO transactionToTransactionDTO(Transaction transaction);
}
