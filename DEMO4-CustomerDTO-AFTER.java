package com.example.demo.dto;

import java.time.LocalDate;

/**
 * DEMO 4 REFERENCE FILE
 * 
 * This is the AFTER version of CustomerDTO for the Fail-Fix-Validate demo.
 * Replace the original CustomerDTO.java with this during the demo to break the tests,
 * then use Copilot to fix them.
 * 
 * Changes from original:
 * 1. Renamed 'phoneNumber' to 'phone'
 * 2. Added new field 'loyaltyPoints'
 * 
 * This will break all 10 tests in CustomerDTOTest.java.
 * Copilot should fix all of them by:
 * - Updating phoneNumber() to phone()
 * - Adding loyaltyPoints parameter with sensible defaults (0, 100, 500, etc.)
 */
public record CustomerDTO(
    Long id,
    String email,
    String firstName,
    String lastName,
    String phone,  // CHANGED from phoneNumber
    LocalDate registrationDate,
    String membershipTier,
    Integer loyaltyPoints  // NEW FIELD
) {
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
