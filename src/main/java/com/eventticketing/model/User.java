
package com.eventticketing.model;

import java.sql.Timestamp;

public class User {

    private int userId;
    private String fullName;
    private String email;
    private String passwordHash;
    private Timestamp createdAt;
    
    /*The first constructor is useful when loading a user from MySQL.*/

    public User(int userId, String fullName, String email,
                String passwordHash, Timestamp createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    /*this constructor is useful when the user is new*/
    public User(String fullName, String email, String passwordHash) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
