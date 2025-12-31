package com.example.demo.service;

import com.example.demo.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private List<User> users;
    
    public UserService() {
        this.users = new ArrayList<>();
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public List<User> getActiveUsers() {
        return users.stream()
            .filter(User::isActive)
            .collect(Collectors.toList());
    }
    
    public User getUserByEmail(String email) {
        return users.stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }
    
    public List<User> getUsersByNamePrefix(String prefix) {
        return users.stream()
            .filter(u -> u.getName().startsWith(prefix))
            .collect(Collectors.toList());
    }
}
