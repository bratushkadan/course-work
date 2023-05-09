package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.AuthToken;
import com.example.model.Cart;
import com.example.model.Product;
import com.example.model.Order;
import com.example.repository.AuthRepository;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.repository.OrderRepository;
import com.example.util.JsonError;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    AuthRepository authRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    OrderRepository orderRepository;

    @GetMapping("")
    public Object getOrders(@RequestParam("auth_token") String authToken) {
        AuthToken token = authRepository.findByAuthToken(authToken);
        if (token == null || AuthToken.isStale(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonError("Invalid credentials."));
        }

        List<Order> orders = orderRepository.findAll(token.getUserId());

        // if (orders.size() != 0) {
        return orders;
        // }

        // return new ArrayList<>();
    }

    @PostMapping("")
    public Object createOrder(@RequestParam("auth_token") String authToken) {
        AuthToken token = authRepository.findByAuthToken(authToken);
        if (token == null || AuthToken.isStale(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonError("Invalid credentials."));
        }

        Cart cart = cartRepository.findByUserId(token.getUserId());

        if (cart == null || cart.getItems().size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonError("Cart is empty."));
        }

        Order order = orderRepository.save(Order.create(cart));

        if (order != null) {
            cartRepository.delete(cart);
        }

        return order;
    }
}
