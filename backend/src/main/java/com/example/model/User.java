package com.example.model;

import org.springframework.data.mongodb.core.mapping.*;

@Document("users")
public class User {

    @MongoId
    private String id;

    private String username;
    private String hashedPassword;

    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
