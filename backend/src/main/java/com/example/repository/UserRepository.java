package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import com.example.model.User;

public interface UserRepository extends MongoRepository<User, String> {
  @Query("{ 'username' : ?0 }")
  public User findByUsername(String username);
}
