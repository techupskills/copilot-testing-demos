package com.example.demo.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerDTOTest {
    
    @Test
    void shouldCreateCustomerWithAllFields() {
        CustomerDTO customer = new CustomerDTO(
            1L,
            "john.doe@example.com",
            "John",
            "Doe",
            "555-1234",
            LocalDate.of(2024, 1, 15),
            "Gold"
        );
        
        assertThat(customer.id()).isEqualTo(1L);
        assertThat(customer.email()).isEqualTo("john.doe@example.com");
        assertThat(customer.firstName()).isEqualTo("John");
        assertThat(customer.lastName()).isEqualTo("Doe");
        assertThat(customer.phoneNumber()).isEqualTo("555-1234");
        assertThat(customer.registrationDate()).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(customer.membershipTier()).isEqualTo("Gold");
    }
    
    @Test
    void shouldGenerateFullName() {
        CustomerDTO customer = new CustomerDTO(
            2L,
            "jane.smith@example.com",
            "Jane",
            "Smith",
            "555-5678",
            LocalDate.of(2024, 2, 20),
            "Platinum"
        );
        
        assertThat(customer.getFullName()).isEqualTo("Jane Smith");
    }
    
    @Test
    void shouldCreatePremiumCustomer() {
        CustomerDTO customer = new CustomerDTO(
            3L,
            "premium@example.com",
            "Premium",
            "User",
            "555-9999",
            LocalDate.of(2023, 12, 1),
            "Platinum"
        );
        
        assertThat(customer.membershipTier()).isEqualTo("Platinum");
        assertThat(customer.email()).isEqualTo("premium@example.com");
    }
    
    @Test
    void shouldHandleBasicMembership() {
        CustomerDTO customer = new CustomerDTO(
            4L,
            "basic@example.com",
            "Basic",
            "Member",
            "555-0000",
            LocalDate.of(2024, 3, 1),
            "Bronze"
        );
        
        assertThat(customer.membershipTier()).isEqualTo("Bronze");
    }
    
    @Test
    void shouldStoreRegistrationDate() {
        LocalDate registrationDate = LocalDate.of(2024, 1, 1);
        CustomerDTO customer = new CustomerDTO(
            5L,
            "newuser@example.com",
            "New",
            "User",
            "555-1111",
            registrationDate,
            "Silver"
        );
        
        assertThat(customer.registrationDate()).isEqualTo(registrationDate);
    }
    
    @Test
    void shouldStorePhoneNumber() {
        CustomerDTO customer = new CustomerDTO(
            6L,
            "contact@example.com",
            "Contact",
            "Person",
            "555-2222",
            LocalDate.of(2024, 2, 15),
            "Gold"
        );
        
        assertThat(customer.phoneNumber()).isEqualTo("555-2222");
    }
    
    @Test
    void shouldCompareCustomersById() {
        CustomerDTO customer1 = new CustomerDTO(
            7L,
            "test1@example.com",
            "Test",
            "One",
            "555-3333",
            LocalDate.of(2024, 1, 10),
            "Bronze"
        );
        
        CustomerDTO customer2 = new CustomerDTO(
            7L,
            "test1@example.com",
            "Test",
            "One",
            "555-3333",
            LocalDate.of(2024, 1, 10),
            "Bronze"
        );
        
        assertThat(customer1).isEqualTo(customer2);
    }
    
    @Test
    void shouldHandleDifferentMembershipTiers() {
        CustomerDTO bronze = new CustomerDTO(1L, "b@example.com", "B", "User", "111", LocalDate.now(), "Bronze");
        CustomerDTO silver = new CustomerDTO(2L, "s@example.com", "S", "User", "222", LocalDate.now(), "Silver");
        CustomerDTO gold = new CustomerDTO(3L, "g@example.com", "G", "User", "333", LocalDate.now(), "Gold");
        CustomerDTO platinum = new CustomerDTO(4L, "p@example.com", "P", "User", "444", LocalDate.now(), "Platinum");
        
        assertThat(bronze.membershipTier()).isEqualTo("Bronze");
        assertThat(silver.membershipTier()).isEqualTo("Silver");
        assertThat(gold.membershipTier()).isEqualTo("Gold");
        assertThat(platinum.membershipTier()).isEqualTo("Platinum");
    }
    
    @Test
    void shouldStoreEmailCorrectly() {
        CustomerDTO customer = new CustomerDTO(
            8L,
            "important@example.com",
            "Important",
            "Customer",
            "555-4444",
            LocalDate.of(2024, 1, 20),
            "Platinum"
        );
        
        assertThat(customer.email()).isEqualTo("important@example.com");
        assertThat(customer.email()).contains("@");
    }
    
    @Test
    void shouldHandleRecentRegistrations() {
        LocalDate recent = LocalDate.of(2024, 12, 1);
        CustomerDTO customer = new CustomerDTO(
            9L,
            "recent@example.com",
            "Recent",
            "Join",
            "555-5555",
            recent,
            "Bronze"
        );
        
        assertThat(customer.registrationDate()).isEqualTo(recent);
        assertThat(customer.registrationDate()).isAfter(LocalDate.of(2024, 11, 1));
    }
}
