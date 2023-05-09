package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.model.AuthToken;
import com.example.model.Cart;

public interface AuthRepository extends MongoRepository<AuthToken, String> {
  AuthToken save(AuthToken token);

  @Query("{ 'userId' : ?0 }")
  AuthToken findByUserId(String userId);

  @Query("{ 'authToken' : ?0 }")
  AuthToken findByAuthToken(String authToken);
}
