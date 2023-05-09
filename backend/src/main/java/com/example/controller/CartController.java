package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.AuthToken;
import com.example.model.Cart;
import com.example.repository.AuthRepository;
import com.example.repository.CartRepository;
import com.example.util.JsonError;

import java.util.HashMap;

// @CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    AuthRepository authRepository;
    @Autowired
    CartRepository cartRepository;

    @GetMapping("")
    public Object getCart(@RequestParam("auth_token") String authToken) {
        AuthToken token = authRepository.findByAuthToken(authToken);
        if (token == null || AuthToken.isStale(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonError("Invalid credentials."));
        }

        Cart userCart = cartRepository.findByUserId(token.getUserId());

        if (userCart == null) {
            return cartRepository.save(new Cart(new HashMap<String, Integer>(), token.getUserId()));
        }
        return userCart;
    }

    @DeleteMapping("")
    public Object clearCart(@RequestParam("auth_token") String authToken) {
        AuthToken token = authRepository.findByAuthToken(authToken);
        if (token == null || AuthToken.isStale(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonError("Invalid credentials."));
        }

        Cart userCart = cartRepository.findByUserId(token.getUserId());

        if (userCart != null) {
            cartRepository.delete(userCart);
        }

        return new Cart(new HashMap<String, Integer>(), token.getUserId());
    }

    @PostMapping("/{productId}")
    public Object addProduct(@PathVariable("productId") String productId,
            @RequestParam("auth_token") String authToken) {
        AuthToken token = authRepository.findByAuthToken(authToken);
        if (token == null || AuthToken.isStale(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonError("Invalid credentials."));
        }

        Cart userCart = cartRepository.findByUserId(token.getUserId());

        if (userCart == null) {
            userCart = new Cart(new HashMap<String, Integer>(), token.getUserId());
        } else {
            cartRepository.delete(userCart);
        }

        userCart.addItem(productId, 1);
        return cartRepository.save(userCart);
    }

    @DeleteMapping("/{productId}")
    public Object removeProduct(@PathVariable("productId") String productId,
            @RequestParam("auth_token") String authToken) {
        AuthToken token = authRepository.findByAuthToken(authToken);
        if (token == null || AuthToken.isStale(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonError("Invalid credentials."));
        }

        Cart userCart = cartRepository.findByUserId(token.getUserId());

        if (userCart == null) {
            return new Cart(new HashMap<String, Integer>(), token.getUserId());
        }

        cartRepository.delete(userCart);
        userCart.removeItem(productId);
        return cartRepository.save(userCart);
    }
}
