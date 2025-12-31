package com.example.demo.service;

import com.example.demo.model.User;
import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

/**
 * Legacy JUnit 3 test - demonstrates anti-patterns
 */
public class LegacyUserServiceTest extends TestCase {
    
    private UserService service;
    
    public void setUp() {
        service = new UserService();
    }
    
    public void testGetActiveUsers() {
        System.out.println("Testing getActiveUsers...");
        
        // Create test data
        User user1 = new User("john@example.com", "John", true);
        User user2 = new User("jane@example.com", "Jane", true);
        User user3 = new User("bob@example.com", "Bob", false);
        
        List<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);
        
        service.setUsers(allUsers);
        
        List<User> activeUsers = service.getActiveUsers();
        
        // Manual verification
        System.out.println("Active users count: " + activeUsers.size());
        if (activeUsers.size() != 2) {
            fail("Expected 2 active users, got " + activeUsers.size());
        }
        
        // Check each user manually
        boolean foundJohn = false;
        boolean foundJane = false;
        for (User u : activeUsers) {
            if (u.getEmail().equals("john@example.com")) {
                foundJohn = true;
            }
            if (u.getEmail().equals("jane@example.com")) {
                foundJane = true;
            }
        }
        
        if (!foundJohn) {
            fail("John should be in active users");
        }
        if (!foundJane) {
            fail("Jane should be in active users");
        }
        
        System.out.println("Test passed!");
    }
    
    public void testGetUserByEmail() {
        System.out.println("Testing getUserByEmail...");
        
        User user1 = new User("test@example.com", "Test User", true);
        List<User> users = new ArrayList<>();
        users.add(user1);
        service.setUsers(users);
        
        User found = service.getUserByEmail("test@example.com");
        
        if (found == null) {
            fail("User should be found");
        }
        
        if (!found.getEmail().equals("test@example.com")) {
            fail("Email doesn't match");
        }
        
        if (!found.getName().equals("Test User")) {
            fail("Name doesn't match");
        }
        
        System.out.println("Test passed!");
    }
    
    public void testGetUsersByNamePrefix() {
        System.out.println("Testing getUsersByNamePrefix...");
        
        User user1 = new User("alice@example.com", "Alice Smith", true);
        User user2 = new User("anna@example.com", "Anna Johnson", true);
        User user3 = new User("bob@example.com", "Bob Williams", true);
        
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        service.setUsers(users);
        
        List<User> results = service.getUsersByNamePrefix("A");
        
        System.out.println("Found " + results.size() + " users");
        
        if (results.size() != 2) {
            fail("Should find 2 users starting with A");
        }
        
        // Check names manually
        for (int i = 0; i < results.size(); i++) {
            User u = results.get(i);
            System.out.println("User " + i + ": " + u.getName());
            if (!u.getName().startsWith("A")) {
                fail("User name should start with A: " + u.getName());
            }
        }
        
        System.out.println("Test passed!");
    }
}
