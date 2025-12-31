# GitHub Copilot Testing Demos

Complete code examples for demonstrating GitHub Copilot's testing capabilities to a Java Users Group.

## Prerequisites

- Java 17 or later
- Maven 3.6+
- GitHub Copilot subscription (for running the demos)
- An IDE with GitHub Copilot extension (IntelliJ IDEA, VS Code, etc.)

## Quick Start

1. **Unzip this project**
2. **Open in your IDE**
3. **Run Maven install**:
   ```bash
   mvn clean install
   ```
4. **Verify all tests pass**:
   ```bash
   mvn test
   ```

## Project Structure

```
src/main/java/com/example/
├── demo/
│   ├── client/          # Demo 1 - Client interfaces
│   ├── dto/             # Demo 4 - Customer DTO
│   ├── model/           # Demo 1, 3 - Domain models
│   ├── repository/      # Demo 1 - Repositories
│   ├── service/         # Demo 1, 3 - Services
│   └── validator/       # Demo 2 - Validators
└── banking/
    ├── exception/       # Demo 5 - Banking exceptions
    ├── model/           # Demo 5 - Banking models
    ├── repository/      # Demo 5 - Banking repositories
    └── service/         # Demo 5 - Banking services

src/test/java/com/example/
├── demo/
│   ├── dto/             # Demo 4 - Customer DTO tests
│   └── service/         # Demo 3 - Legacy test
└── banking/
    └── service/         # Demo 5 - Integration tests
```

## Demo Instructions

### Demo 1: Zero-to-JUnit-5 Scaffolding

**File**: `src/main/java/com/example/demo/service/OrderService.java`

**Steps**:
1. Open `OrderService.java`
2. Position cursor inside the class
3. Open GitHub Copilot Chat (Ctrl+I or Cmd+I)
4. Type: `@workspace /tests Create a JUnit 5 test for this service using Mockito. Use @ExtendWith(MockitoExtension.class) and create a setup method for mocking the repository and clients.`
5. Watch Copilot generate the complete test structure

**Expected Output**: Complete test class with:
- `@ExtendWith(MockitoExtension.class)`
- `@Mock` annotations for all three dependencies
- `@InjectMocks` for OrderService
- `@BeforeEach` setup method
- Test methods for all service methods

---

### Demo 2: Parameterized Tests & Edge Case Discovery

**File**: `src/main/java/com/example/demo/validator/LoanEligibilityValidator.java`

**Steps**:
1. Open `LoanEligibilityValidator.java`
2. Highlight the `isEligible` method
3. Open GitHub Copilot Chat
4. Type: `Generate a JUnit 5 @ParameterizedTest for this method. Include valid cases, boundary cases for credit score/income/debt ratio, null values, negative values, zero values, and edge cases testing threshold limits. Use @MethodSource.`
5. Watch Copilot generate comprehensive test cases

**Expected Output**: Parameterized test with:
- Happy path scenarios
- Null parameter tests
- Boundary values (619 vs 620 credit score, etc.)
- Edge cases at exact thresholds
- Negative and zero value tests

---

### Demo 3: Modernizing Legacy "Spaghetti" Tests

**File**: `src/test/java/com/example/demo/service/LegacyUserServiceTest.java`

**Steps**:
1. Open `LegacyUserServiceTest.java` and point out problems:
   - JUnit 3 style (extends TestCase)
   - System.out.println for debugging
   - Manual loops for assertions
   - Verbose manual checks
2. Select the entire test class
3. Open GitHub Copilot Chat
4. Type: `Refactor this test class to modern JUnit 5 and AssertJ. Replace TestCase with @Test annotations, System.out.println with proper test names, manual loops with AssertJ extracting() and containsExactly(), manual null checks with AssertJ assertions, and fail() calls with assertThat().`
5. Watch the transformation

**Expected Output**: Modern test with:
- `@Test` annotations
- `assertThat(activeUsers).hasSize(2)`
- `assertThat(activeUsers).extracting(User::getEmail).containsExactlyInAnyOrder(...)`
- Clean, fluent assertions

---

### Demo 4: Fail-Fix-Validate Agent Loop

**Files**: 
- `src/main/java/com/example/demo/dto/CustomerDTO.java`
- `src/test/java/com/example/demo/dto/CustomerDTOTest.java`

**Steps**:
1. Run all tests - verify they pass (green)
2. Modify `CustomerDTO.java` record:
   ```java
   public record CustomerDTO(
       Long id,
       String email,
       String firstName,
       String lastName,
       String phone,  // CHANGED from phoneNumber
       LocalDate registrationDate,
       String membershipTier,
       Integer loyaltyPoints  // NEW FIELD
   ) { ... }
   ```
3. Run tests - watch them fail (compilation errors)
4. Click "Fix with Copilot" or use Copilot Chat: `These tests are failing because I renamed phoneNumber to phone and added a loyaltyPoints field. Update all test data and assertions.`
5. Watch Copilot bulk-update all test cases
6. Run tests - they should pass again

**Expected Output**: 
- All 10 test methods updated
- `phoneNumber()` calls changed to `phone()`
- New `loyaltyPoints` parameter added (with reasonable values like 0, 100, 500)
- Tests pass

---

### Demo 5: AI-First Code Review

**Files**: 
- `src/main/java/com/example/banking/service/AccountService.java`
- `src/test/java/com/example/banking/service/AccountServiceIT.java`

**Prerequisites**: 
- GitHub repository with this code
- GitHub Copilot Enterprise enabled

**Steps**:

#### Setup (do before the demo):
1. Push the current code to `main` branch
2. Create a feature branch: `git checkout -b feature/overdraft-protection`
3. Replace `AccountService.java` with the version in `DEMO5-AccountService-AFTER.java` (see below)
4. Commit and push
5. Create a Pull Request

#### Demo Flow:
1. Navigate to the PR in GitHub web UI
2. Point out the Copilot-generated PR summary
3. Go to "Files changed" tab
4. Click "Review with Copilot" or wait for auto-review
5. Show AI-generated comments:
   - Missing test coverage for new overdraft feature
   - Bug in line with `>` comparison (should be `>=`)
6. Click "Generate Tests" to see suggested test cases
7. Click "Commit suggestion" for the bug fix
8. Show tests passing after fixes

**Expected Copilot Comments**:
- "Significant logic changes but no corresponding test coverage in AccountServiceIT.java"
- "Potential bug - allows overdraft of $500.01 when limit is $500.00"
- Suggests boundary tests, overdraft fee tests, different account type tests

---

## Demo 5: Modified AccountService.java (for PR)

Save this as a reference for Demo 5. Replace `AccountService.java` with this code for the PR:

```java
package com.example.banking.service;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import com.example.banking.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

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
        
        if ("PREMIUM_CHECKING".equals(account.getAccountType())) {
            // BUG: Should be >= not >
            if (newBalance.compareTo(OVERDRAFT_LIMIT.negate()) > 0) {
                account.setBalance(newBalance);
                
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
```

## Troubleshooting

### Tests won't compile
- Verify you have Java 17+: `java -version`
- Run `mvn clean install` to download dependencies

### Copilot not generating tests
- Make sure GitHub Copilot extension is installed and authenticated
- Try rephrasing your prompt
- Ensure you're using `@workspace /tests` for context

### Demo 5 review not triggering
- Verify GitHub Copilot Enterprise is enabled on your repository
- Manually trigger review using the Copilot button in the PR toolbar
- As a fallback, use Copilot Chat in IDE with the diff

## Tips for Presentation

1. **Practice the prompts** - exact wording matters
2. **Have backup screenshots** in case live demo fails
3. **Run Demo 4 last** - it's the most impressive when tests actually fail
4. **Keep a reset script** to quickly restore original code between demos

## Key Talking Points

- **Demo 1**: "Copilot as a setup wizard - no more copying test templates"
- **Demo 2**: "Copilot as a QA partner - finding edge cases you'd miss"
- **Demo 3**: "Copilot as a refactoring tool - technical debt paydown"
- **Demo 4**: "Copilot as an agent - understanding failure and fixing it"
- **Demo 5**: "Copilot as a team safety net - living in your PR workflow"

## License

MIT License - Free to use for training and demonstrations

## Questions?

Feel free to modify these examples for your presentation!
