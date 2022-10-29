package com.novruz.account.mapper;

import com.novruz.account.model.Account;
import com.novruz.account.dto.AccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDTO toAccountDTO(Account account);

    Account toAccount(AccountDTO accountDTO);
}
