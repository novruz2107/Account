package com.novruz.account.service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.novruz.account.dto.AccountDTO;
import com.novruz.account.dto.BalanceDTO;
import com.novruz.account.dto.request.CreateTransactionRequest;
import com.novruz.account.enums.CurrencyType;
import com.novruz.account.enums.TransactionDirection;
import com.novruz.account.exception.BadRequestException;
import com.novruz.account.exception.NotFoundException;
import com.novruz.account.exception.OperationInvalidException;
import com.novruz.account.mapper.AccountMapper;
import com.novruz.account.mapper.BalanceMapper;
import com.novruz.account.mapper.TransactionMapper;
import com.novruz.account.model.Account;
import com.novruz.account.model.Balance;
import com.novruz.account.model.Transaction;
import com.novruz.account.repository.AccountRepository;
import com.novruz.account.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {
    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    MessagePublisher messagePublisher;

    AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(accountRepository, transactionRepository, messagePublisher);
    }

    @Test
    @DisplayName("createAccount should return success if input is valid")
    void createAccount_inputIsValid_ShouldReturnSuccess() {
        //arrange
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCustomerId(1L);
        BalanceDTO balanceDTO = new BalanceDTO();
        balanceDTO.setAmount(0);
        balanceDTO.setCurrency(CurrencyType.USD);
        List<BalanceDTO> balanceDTOList = new ArrayList<>();
        balanceDTOList.add(balanceDTO);
        accountDTO.setBalanceList(balanceDTOList);

        Balance balance = BalanceMapper.INSTANCE.toBalance(balanceDTO);
        balance.setAccountId(1);

        when(accountRepository.insertAccount(AccountMapper.INSTANCE.toAccount(accountDTO))).thenReturn(1);

        //act
        var response = accountService.createAccount(accountDTO);

        //assert
        assertEquals(1, response.getAccountId());
        assertEquals(1, response.getBalanceList().size());
        assertEquals(CurrencyType.USD, response.getBalanceList().get(0).getCurrency());
    }

    @Test
    @DisplayName("createAccount should throw bad request if input is invalid")
    void createAccount_inputIsInvalid_ShouldThrowBadRequest() {
        //arrange
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCustomerId(1L);
        List<BalanceDTO> balanceDTOList = new ArrayList<>();
        accountDTO.setBalanceList(balanceDTOList);

        //act

        //assert
        assertThrowsExactly(BadRequestException.class, () -> accountService.createAccount(accountDTO), "Bad request");
    }

    @Test
    @DisplayName("getAccountById should return account if account is found")
    void getAccountById_ifAccountFound_ShouldReturnAccount() {
        //arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setCustomerId(1);
        Balance balance = new Balance();
        balance.setAmount(0);
        balance.setCurrency(CurrencyType.USD.value());
        List<Balance> balanceList = new ArrayList<>();
        balanceList.add(balance);
        account.setBalanceList(balanceList);

        when(accountRepository.getAccountById(1L)).thenReturn(account);

        //act
        var response = accountService.getAccountById(1);

        //assert
        assertEquals(1, response.getAccountId());
        assertEquals(1, response.getBalanceList().size());
        assertEquals(CurrencyType.USD, response.getBalanceList().get(0).getCurrency());
    }

    @Test
    @DisplayName("getAccountById should throw not found if account is not found")
    void getAccountById_ifAccountNotFound_ShouldThrowNotFound() {
        //arrange
        when(accountRepository.getAccountById(1L)).thenReturn(null);

        //act

        //assert
        assertThrowsExactly(NotFoundException.class, () -> accountService.getAccountById(1),
                "Account not found");
    }

    @Test
    @DisplayName("createTransaction should throw not found if account is not found")
    void createTransaction_ifAccountNotFound_ShouldThrowNotFound() {
        //arrange
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);

        when(accountRepository.getAccountById(1L)).thenReturn(null);

        //act

        //assert
        assertThrowsExactly(NotFoundException.class, () -> accountService.createTransaction(request),
                "Account missing");
    }

    @Test
    @DisplayName("createTransaction should throw not found if currency is not found")
    void createTransaction_ifCurrencyNotFound_ShouldThrowNotFound() {
        //arrange
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setCurrency(CurrencyType.EUR);

        Account account = new Account();
        account.setAccountId(1);
        account.setCustomerId(1);
        Balance balance = new Balance();
        balance.setAmount(0);
        balance.setCurrency(CurrencyType.USD.value());
        List<Balance> balanceList = new ArrayList<>();
        balanceList.add(balance);
        account.setBalanceList(balanceList);

        when(accountRepository.getAccountById(1L)).thenReturn(account);

        //act

        //assert
        assertThrowsExactly(NotFoundException.class, () -> accountService.createTransaction(request),
                "No balance found with the currency");
    }

    @Test
    @DisplayName("createTransaction should throw operation invalid if funds are insufficient")
    void createTransaction_ifInsufficientFunds_ShouldThrowOperationInvalid() {
        //arrange
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setCurrency(CurrencyType.USD);
        request.setAmount(50);
        request.setTransactionDirection(TransactionDirection.OUT);
        request.setDescription("description");

        Account account = new Account();
        account.setAccountId(1);
        account.setCustomerId(1);
        Balance balance = new Balance();
        balance.setAmount(0);
        balance.setCurrency(CurrencyType.USD.value());
        List<Balance> balanceList = new ArrayList<>();
        balanceList.add(balance);
        account.setBalanceList(balanceList);

        when(accountRepository.getAccountById(1L)).thenReturn(account);

        //act

        //assert
        assertThrowsExactly(OperationInvalidException.class, () -> accountService.createTransaction(request),
                "Insufficient funds");
    }

    @Test
    @DisplayName("createTransaction should create new transaction if conditions are okay")
    void createTransaction_ifConditionsOk_ShouldCreateTransaction() {
        //arrange
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setCurrency(CurrencyType.USD);
        request.setAmount(50);
        request.setTransactionDirection(TransactionDirection.IN);
        request.setDescription("description");

        Account account = new Account();
        account.setAccountId(1);
        account.setCustomerId(1);
        Balance balance = new Balance();
        balance.setAmount(0);
        balance.setCurrency(CurrencyType.USD.value());
        List<Balance> balanceList = new ArrayList<>();
        balanceList.add(balance);
        account.setBalanceList(balanceList);

        when(accountRepository.getAccountById(1L)).thenReturn(account);
        when(transactionRepository
                .insertTransaction(TransactionMapper.INSTANCE.transactionRequestToTransaction(request))).thenReturn(1);

        //act
        var response = accountService.createTransaction(request);

        //assert
        assertEquals(50, response.getBalance().getAmount());
        assertEquals(1, response.getTransaction().getTransactionId());
        assertEquals(50, response.getTransaction().getAmount());
        assertEquals(CurrencyType.USD, response.getTransaction().getCurrency());
    }

    @Test
    @DisplayName("getTransactionsByAccountId should throw not found if balance is not found")
    void getTransactionsByAccountId_ifBalanceNotFound_ShouldThrowNotFound() {
        //arrange
        when(accountRepository.getBalancesByAccountId(1)).thenReturn(null);

        //act

        //assert
        assertThrowsExactly(NotFoundException.class, () -> accountService.getTransactionsByAccountId(1),
                "No balance found");
    }

    @Test
    @DisplayName("getTransactionsByAccountId should return transactions if conditions are okay")
    void getTransactionsByAccountId_ifConditionsOk_ShouldReturnTransactions() {
        //arrange
        Balance balance = new Balance();
        balance.setCurrency(CurrencyType.USD.value());
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        balance.setTransactions(transactionList);
        List<Balance> balanceList = new ArrayList<>();
        balanceList.add(balance);

        when(transactionRepository.getBalancesByAccountId(1)).thenReturn(balanceList);

        //act
        var response = accountService.getTransactionsByAccountId(1);

        //assert
        assertEquals(1, response.getTransactions().size());
        assertEquals(CurrencyType.USD, response.getTransactions().get(0).getCurrency());
        assertEquals(1, response.getTransactions().get(0).getAccountId());
    }
}