package com.example.demo.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoanEligibilityValidator {
    
    private static final BigDecimal MIN_CREDIT_SCORE = new BigDecimal("620");
    private static final BigDecimal MAX_DEBT_TO_INCOME_RATIO = new BigDecimal("0.43");
    private static final BigDecimal MIN_INCOME = new BigDecimal("25000");
    private static final int MIN_EMPLOYMENT_MONTHS = 12;
    
    /**
     * Validates loan eligibility based on multiple criteria.
     * 
     * @param creditScore Credit score (300-850)
     * @param annualIncome Annual income in dollars
     * @param monthlyDebtPayments Total monthly debt payments
     * @param employmentMonths Months at current employer
     * @return true if eligible, false otherwise
     */
    public boolean isEligible(BigDecimal creditScore, 
                             BigDecimal annualIncome, 
                             BigDecimal monthlyDebtPayments,
                             int employmentMonths) {
        
        // Null checks - a deficiency to be caught by testing!
        if (creditScore == null || annualIncome == null || monthlyDebtPayments == null) {
            return false;
        }
        
        // Credit score check
        if (creditScore.compareTo(MIN_CREDIT_SCORE) < 0) {
            return false;
        }
        
        // Income check
        if (annualIncome.compareTo(MIN_INCOME) < 0) {
            return false;
        }
        
        // Employment stability check
        if (employmentMonths < MIN_EMPLOYMENT_MONTHS) {
            return false;
        }
        
        // Debt-to-income ratio check
        BigDecimal monthlyIncome = annualIncome.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal debtToIncomeRatio = monthlyDebtPayments.divide(monthlyIncome, 4, RoundingMode.HALF_UP);
        
        return debtToIncomeRatio.compareTo(MAX_DEBT_TO_INCOME_RATIO) <= 0;
    }
}
