package com.example.banking.service;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import com.example.banking.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * Service for managing bank accounts, including deposits, withdrawals, and balance retrieval.
 *
 * <p>For PREMIUM_CHECKING accounts, limited overdraft is supported up to a configured
 * overdraft limit, and an overdraft fee is applied when the account balance becomes
 * negative. Standard account types do not allow overdrafts.</p>
 */
@Service
public class AccountService {
    
    private final AccountRepository accountRepository;
    private static final BigDecimal OVERDRAFT_LIMIT = new BigDecimal("500.00");
    private static final BigDecimal OVERDRAFT_FEE = new BigDecimal("35.00");
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    @Transactional
    public Account withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        
        BigDecimal newBalance = account.getBalance().subtract(amount);
        
        // Check if withdrawal is allowed
        if ("PREMIUM_CHECKING".equals(account.getAccountType())) {
            // BUG FIX: Use >= for the limit check so -$500.00 is allowed but not below
            // This ensures withdrawals are allowed up to and including the overdraft limit
            if (newBalance.compareTo(OVERDRAFT_LIMIT.negate()) >= 0) {
                account.setBalance(newBalance);
                
                // Apply overdraft fee if balance goes negative
                if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    account.setBalance(account.getBalance().subtract(OVERDRAFT_FEE));
                }
                
                return accountRepository.save(account);
            } else {
                throw new InsufficientFundsException(
                    "Withdrawal would exceed overdraft limit of $" + OVERDRAFT_LIMIT
                );
            }
        } else {
            // Standard accounts: no overdraft allowed
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException("Insufficient funds for withdrawal");
            }
            
            account.setBalance(newBalance);
            return accountRepository.save(account);
        }
    }
    
    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }
    
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
    }
    
    public BigDecimal getOverdraftLimit(Account account) {
        return "PREMIUM_CHECKING".equals(account.getAccountType()) 
            ? OVERDRAFT_LIMIT 
            : BigDecimal.ZERO;
    }
}
