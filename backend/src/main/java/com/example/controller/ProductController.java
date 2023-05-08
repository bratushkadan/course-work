package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.util.Password;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/api/products")
    public Product addProduct(@RequestParam("name") String name, @RequestParam("price") String price,
            @RequestParam("description") String description, @RequestParam("image") String image) {
        return productRepository.save(new Product(name, Integer.parseInt(price), description, image));
    }

    @GetMapping("/api/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
