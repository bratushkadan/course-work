package com.example.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Cart;
import com.example.model.Product;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CartController {
    @GetMapping("/api/cart")
    public List<Product> getCart(@RequestParam("authToken") String authToken) {

        if (authToken == "123") {
            // Cart cart = cartRepository.findByUserName("danila");
        
            return Arrays.asList(new Product("Говядина", 400, "Свежая говядина",
                    "https://ketokotleta.ru/wp-content/uploads/7/d/9/7d9421d8fb9ca8b972feea4bdf854eb2.jpeg"));
        }

        return Arrays.asList();
    }

}
