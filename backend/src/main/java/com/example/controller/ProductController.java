package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Product;
import com.example.repository.ProductRepository;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    /* @PostMapping("/api/products")
    public Product addProduct(@RequestParam("name") String name, @RequestParam("price") String price,
            @RequestParam("description") String description, @RequestParam("image") String image) {
        return productRepository.save(new Product(name, Integer.parseInt(price), description, image));
    } */

    @GetMapping("/api/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
