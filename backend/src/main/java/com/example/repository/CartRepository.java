package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.model.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {
  // Cart findByUserName(String username);
}
