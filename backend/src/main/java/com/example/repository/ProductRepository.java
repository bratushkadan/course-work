package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
  List<Product> findAll();

  public long count();
}
