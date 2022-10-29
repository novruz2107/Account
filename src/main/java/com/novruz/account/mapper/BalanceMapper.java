package com.novruz.account.mapper;

import com.novruz.account.model.Balance;
import com.novruz.account.dto.BalanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BalanceMapper {
    BalanceMapper INSTANCE = Mappers.getMapper(BalanceMapper.class);

    BalanceDTO toBalanceDTO(Balance balance);

    Balance toBalance(BalanceDTO balanceDTO);
}
