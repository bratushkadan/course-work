package com.example.model;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("carts")
public class Cart {
    private class CartItem {
        private String productId;
        private int quantity;

        public CartItem(String productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public String getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    private Map<String, Integer> items;
    private String userId;

    public Cart(Map<String, Integer> items, String userId) {
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

    public ArrayList<CartItem> getItems() {
        return items;
    }
}
