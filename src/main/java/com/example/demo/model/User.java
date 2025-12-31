package com.example.demo.model;

public class User {
    private String email;
    private String name;
    private boolean active;
    
    public User(String email, String name, boolean active) {
        this.email = email;
        this.name = name;
        this.active = active;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}
