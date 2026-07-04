package com.auth.model;

import java.time.Instant;

public class User {
    private String id;
    private String email;
    private String passwordHash;
    private String name;
    private String[] roles;
    private Instant createdAt;

    public User() {}

    public User(String id, String email, String passwordHash, String name, String[] roles, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String[] getRoles() { return roles; }
    public void setRoles(String[] roles) { this.roles = roles; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
