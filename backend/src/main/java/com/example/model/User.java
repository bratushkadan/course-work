package com.example.model;

import org.springframework.data.mongodb.core.mapping.*;

@Document("users")
public class User {

    @MongoId
    private String id;

    private String username;
    private String hashedPassword;
    private String tel;

    public User(String username, String hashedPassword, String tel) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public String getTel() {
        return tel;
    }

    public String getUsername() {
        return username;
    }
}
