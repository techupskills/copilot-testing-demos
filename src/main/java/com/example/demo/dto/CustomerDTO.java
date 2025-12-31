package com.example.demo.dto;

import java.time.LocalDate;

public record CustomerDTO(
    Long id,
    String email,
    String firstName,
    String lastName,
    String phoneNumber,
    LocalDate registrationDate,
    String membershipTier
) {
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
