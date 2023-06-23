package org.example.model.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Product {
    private String name;
    private double price;

    public Product() {
    }
}
