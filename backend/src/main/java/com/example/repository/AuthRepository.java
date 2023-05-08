package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.model.Cart;

public interface AuthRepository extends MongoRepository<Cart, String> {

  @Query("{ 'username' : ?0, 'hashedPassword' : ?1 }")
  Cart findByCredentials(String username, String hashedPassword);
}
