package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

  // @Aggregation(pipeline = {
  // "{ $group: { _id: $userId, count: { $sum: 1 } } }",
  // "{ $match: { count: { $gt: 1 } } }"
  // })
  // Order findAllByUserId(String userId);

  @Query("{ 'userId' : ?0 }")
  List<Order> findAll(String userId);
}
