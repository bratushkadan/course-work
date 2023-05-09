package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.*;

@Document("carts")
public class Cart {

    @MongoId
    private String id;
    
    private HashMap<String, Integer> items;
    private String userId;

    public Cart(HashMap<String, Integer> items, String userId) {
        this.items = items;
        this.userId = userId;
    }

    public Cart addItem(String productId, int quantity) {
        items.put(productId, items.getOrDefault(productId, 0) + quantity);
        return this;
    }

    public Cart removeItem(String productId) {
        items.remove(productId);
        return this;
    }

    public Cart clearCart() {
        items.clear();
        return this;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, Integer> getItems() {
        return items;
    }

    public String getUserId() {
        return userId;
    }
}
