package com.example.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("carts")
public class Cart {
    private Map<String, Integer> items;
    private String userId;

    public Cart(String userId) {
        this.items = new HashMap<>();
        this.userId = userId;
    }

    public void addItem(String productId, int quantity) {
        items.put(productId, items.getOrDefault(productId, 0) + quantity);
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void clearCart() {
        items.clear();
    }

    public void syncCart() {
        // MongoClient mongoClient = new MongoClient("localhost", 27017);
        // MongoDatabase database = mongoClient.getDatabase("mydb");

    }
}
