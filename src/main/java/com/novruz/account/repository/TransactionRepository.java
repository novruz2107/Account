package com.novruz.account.repository;

import com.novruz.account.model.Balance;
import com.novruz.account.model.Transaction;
import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TransactionRepository {

    @Select("INSERT INTO transactions(balance_id, amount, direction, description)" +
            " VALUES(#{balanceId}, #{amount}, #{transactionDirection}, #{description}) " +
            "RETURNING transaction_id")
    int insertTransaction(Transaction transaction);

    @Select("SELECT account_id, balance_id, currency" +
            " FROM balances where account_id=#{accountId}")
    @Results(value = {
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "balanceId", column = "balance_id"),
            @Result(property = "transactions", column = "balance_id", javaType = List.class,
                    many = @Many(select = "getTransactionsByBalanceId"))
    })
    List<Balance> getBalancesByAccountId(long accountId);

    @Select("SELECT * FROM transactions where balance_id=#{balanceId}")
    @Results(value = {
            @Result(property = "transactionId", column = "transaction_id"),
            @Result(property = "transactionDirection", column = "direction"),
    })
    List<Transaction> getTransactionsByBalanceId(long accountId);
}
