package com.example.model;

import com.example.model.Product;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.*;

@Document("orders")
public class Order {
    public static class OrderProduct {
        private String productId;
        private Integer quantity;

        public OrderProduct(String productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public String getProductId() {
            return productId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }

    @MongoId
    private String id;

    private long timestamp;

    private String userId;
    private ArrayList<Order.OrderProduct> products;

    public Order(String userId, ArrayList<Order.OrderProduct> products) {
        super();

        this.userId = userId;
        this.products = products;
        this.timestamp = System.currentTimeMillis();
    }

    public static final Order create(Cart cart) {
        Map<String, Integer> cartItems = cart.getItems();
        ArrayList<Order.OrderProduct> products = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            products.add(new Order.OrderProduct(entry.getKey(), entry.getValue()));
        }
        
        return new Order(cart.getUserId(), products);
    }

    public ArrayList<Order.OrderProduct> getProducts() {
        return products;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
