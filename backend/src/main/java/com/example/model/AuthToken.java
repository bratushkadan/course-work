package com.example.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.*;

@Document("auth_tokens")
public class AuthToken {

    @MongoId
    private String id;
    @CreatedDate
    private Integer timestamp;

    private String authToken;
    private String username;
    private String hashedPassword;

    public AuthToken(String authToken, String username, String hashedPassword) {
        super();
        this.authToken = authToken;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public String getId() {
        return id;
    }

    public String getAuthToken() {
        return authToken;
    }
}
