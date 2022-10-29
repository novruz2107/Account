package com.novruz.account.service;

import com.novruz.account.dto.AccountDTO;
import com.novruz.account.dto.request.CreateTransactionRequest;
import com.novruz.account.dto.response.CreateTransactionResponse;
import com.novruz.account.dto.response.GetTransactionsResponse;
import com.novruz.account.exception.BadRequestException;
import com.novruz.account.exception.NotFoundException;
import com.novruz.account.mapper.AccountMapper;
import com.novruz.account.mapper.BalanceMapper;
import com.novruz.account.mapper.TransactionMapper;
import com.novruz.account.model.Balance;
import com.novruz.account.model.Transaction;
import com.novruz.account.repository.AccountRepository;
import com.novruz.account.repository.TransactionRepository;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final MessagePublisher messagePublisher;

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        if (accountDTO.getBalanceList().isEmpty()) {
            throw new BadRequestException("Bad request");
        }
        var accountId = accountRepository.insertAccount(AccountMapper.INSTANCE.toAccount(accountDTO));
        for (var balance : accountDTO.getBalanceList()) {
            Balance balanceDAO = BalanceMapper.INSTANCE.toBalance(balance);
            balanceDAO.setAccountId(accountId);
            accountRepository.insertBalance(balanceDAO);
        }
        accountDTO.setAccountId(accountId);
        messagePublisher.publishAccount(accountDTO.toString());

        return accountDTO;
    }

    @Override
    public AccountDTO getAccountById(int accountId) {
        log.info("getAccountById.start accountId:{}", accountId);
        var result = accountRepository.getAccountById(accountId);

        if (ObjectUtils.isEmpty(result))
            throw new NotFoundException("Account not found");

        log.info("accountDAO: {}", result);
        return AccountMapper.INSTANCE.toAccountDTO(result);
    }

    @Override
    @Transactional
    public CreateTransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest) {
        var account = accountRepository.getAccountById(createTransactionRequest.getAccountId());
        if (ObjectUtils.isEmpty(account)) {
            throw new NotFoundException("Account missing");
        }

        var balanceOptional = account.getBalanceList().stream()
                .filter(b -> b.getCurrency().equals(createTransactionRequest.getCurrency().value())).findFirst();

        var balance = balanceOptional.or(() -> {
            throw new NotFoundException("No balance found with the currency");
        }).get();

        balance.applyTransaction(createTransactionRequest.getAmount(),
                createTransactionRequest.getTransactionDirection());
        accountRepository.updateBalanceAmount(balance);

        Transaction transaction =
                TransactionMapper.INSTANCE.transactionRequestToTransaction(createTransactionRequest);
        transaction.setBalanceId(balance.getBalanceId());

        int transactionId = transactionRepository.insertTransaction(transaction);
        transaction.setTransactionId(transactionId);

        var response = new CreateTransactionResponse(
                TransactionMapper.INSTANCE.transactionToTransactionDTO(transaction),
                BalanceMapper.INSTANCE.toBalanceDTO(balance));
        messagePublisher.publishTransaction(response.toString());
        return response;
    }

    @Override
    public GetTransactionsResponse getTransactionsByAccountId(long accountId) {
        var balances = transactionRepository.getBalancesByAccountId(accountId);
        if (balances.isEmpty())
            throw new NotFoundException("No balance found");

        log.info("Transactions: {}", balances);

        GetTransactionsResponse response = new GetTransactionsResponse();

        var transactionDTOList = balances.stream().map(balance -> {
            String currency = balance.getCurrency();
            return balance.getTransactions().stream().
                    peek(t -> t.setCurrency(currency)).
                    peek(t -> t.setAccountId(accountId)).
                    map(TransactionMapper.INSTANCE::transactionToTransactionDTO).
                    collect(Collectors.toList());

        }).flatMap(Collection::stream).collect(Collectors.toList());
        response.setTransactions(transactionDTOList);

        return response;
    }
}
