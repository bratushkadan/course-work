package com.example.model;

import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.*;

@Document("auth_tokens")
public class AuthToken {
    private static final long EXPIRATION = 604800000; // 7 days

    @MongoId
    private String id;

    private long timestamp;

    private String authToken;
    private String userId;

    public AuthToken(String authToken, String userId, long timestamp) {
        super();
        this.authToken = authToken;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public static AuthToken create(String userId) {
        return new AuthToken(UUID.randomUUID().toString(), userId, System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static final boolean isStale(AuthToken token) {
        return System.currentTimeMillis() - token.timestamp > AuthToken.EXPIRATION;
    }
}
