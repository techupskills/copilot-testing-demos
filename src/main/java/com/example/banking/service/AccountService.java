package com.example.banking.service;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import com.example.banking.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * DEMO 5 REFERENCE FILE
 * 
 * This is the AFTER version of AccountService with the new overdraft protection feature
 * and a subtle bug. Use this to replace the original AccountService.java when creating
 * the Pull Request for Demo 5.
 * 
 * Changes from original:
 * 1. Added OVERDRAFT_LIMIT and OVERDRAFT_FEE constants
 * 2. Modified withdraw() to support overdraft for PREMIUM_CHECKING accounts
 * 3. Added overdraft fee application when balance goes negative
 * 4. Added getOverdraftLimit() helper method
 * 5. BUG: Line 39 uses > instead of >= allowing $500.01 overdraft when limit is $500.00
 * 
 * What Copilot should catch:
 * - Missing integration tests for the new overdraft feature
 * - The off-by-one bug in the comparison operator
 * - Need for boundary testing at exactly -$500.00
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
            // BUG: Should be >= for the limit check, not >
            // This allows overdraft of $500.01 when limit is $500.00
            if (newBalance.compareTo(OVERDRAFT_LIMIT.negate()) > 0) {
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
