package com.example.banking.service;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import com.example.banking.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountServiceIT {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private AccountRepository accountRepository;
    
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        testAccount = accountRepository.save(
            new Account("ACC-12345", new BigDecimal("1000.00"), "CHECKING")
        );
    }
    
    @Test
    void shouldWithdrawFundsSuccessfully() {
        Account result = accountService.withdraw(testAccount.getId(), new BigDecimal("500.00"));
        
        assertThat(result.getBalance()).isEqualByComparingTo("500.00");
    }
    
    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        assertThatThrownBy(() -> 
            accountService.withdraw(testAccount.getId(), new BigDecimal("1500.00"))
        ).isInstanceOf(InsufficientFundsException.class)
         .hasMessageContaining("Insufficient funds");
    }
    
    @Test
    void shouldDepositFundsSuccessfully() {
        Account result = accountService.deposit(testAccount.getId(), new BigDecimal("500.00"));
        
        assertThat(result.getBalance()).isEqualByComparingTo("1500.00");
    }
    
    @Test
    void shouldRetrieveAccountById() {
        Account result = accountService.getAccount(testAccount.getId());
        
        assertThat(result.getAccountNumber()).isEqualTo("ACC-12345");
        assertThat(result.getBalance()).isEqualByComparingTo("1000.00");
    }
    
    @Test
    void shouldHandleMultipleTransactions() {
        accountService.deposit(testAccount.getId(), new BigDecimal("200.00"));
        accountService.withdraw(testAccount.getId(), new BigDecimal("300.00"));
        
        Account result = accountService.getAccount(testAccount.getId());
        assertThat(result.getBalance()).isEqualByComparingTo("900.00");
    }
    
    @Test
    void shouldReturnOverdraftLimitForPremiumCheckingAccount() {
        Account premiumAccount = accountRepository.save(
            new Account("ACC-PREMIUM", new BigDecimal("1000.00"), "PREMIUM_CHECKING")
        );
        
        BigDecimal overdraftLimit = accountService.getOverdraftLimit(premiumAccount);
        
        assertThat(overdraftLimit).isEqualByComparingTo("500.00");
    }
    
    @Test
    void shouldReturnZeroOverdraftLimitForNonPremiumAccount() {
        BigDecimal overdraftLimit = accountService.getOverdraftLimit(testAccount);
        
        assertThat(overdraftLimit).isEqualByComparingTo("0.00");
    }
    
    @Test
    void shouldReturnZeroOverdraftLimitForSavingsAccount() {
        Account savingsAccount = accountRepository.save(
            new Account("ACC-SAVINGS", new BigDecimal("2000.00"), "SAVINGS")
        );
        
        BigDecimal overdraftLimit = accountService.getOverdraftLimit(savingsAccount);
        
        assertThat(overdraftLimit).isEqualByComparingTo("0.00");
    }
}
