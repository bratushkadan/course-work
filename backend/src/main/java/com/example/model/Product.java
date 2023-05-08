package com.example.model;

import org.springframework.data.mongodb.core.mapping.*;

@Document("products")
public class Product {

    @MongoId
    private String id;

    private String name;
    private int price;
    private String description;
    private String image;

    public Product(String name, int price, String description, String image) {
        super();
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
