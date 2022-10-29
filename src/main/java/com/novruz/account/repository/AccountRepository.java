package com.novruz.account.repository;

import com.novruz.account.model.Account;
import com.novruz.account.model.Balance;
import com.novruz.account.model.Transaction;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccountRepository {

    @Select("INSERT INTO accounts(customer_id) VALUES(#{customerId}) RETURNING account_id")
    int insertAccount(Account account);

    @Insert("INSERT INTO balances(account_id, currency, amount)" +
            " VALUES(#{accountId}, #{currency}, #{amount})")
    void insertBalance(Balance balance);

    @Select("SELECT account_id, customer_id FROM accounts where account_id=#{accountId}")
    @Results(value = {
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "balanceList", column = "account_id", javaType = List.class,
                    many = @Many(select = "getBalancesByAccountId"))
    })
    Account getAccountById(long accountId);

    @Select("SELECT * FROM balances where account_id=#{accountId}")
    @Results(value = {
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "balanceId", column = "balance_id")
    })
    List<Balance> getBalancesByAccountId(int accountId);

    @Update("UPDATE balances SET amount=#{amount} where balance_id=#{balanceId}")
    void updateBalanceAmount(Balance balance);
}
