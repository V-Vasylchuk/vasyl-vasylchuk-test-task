package org.example.model.product;

import lombok.Getter;

@Getter
public class RealProduct extends Product {
    private int size;
    private int weight;

    public RealProduct(String name, double price, int size, int weight) {
        super(name, price);
        this.size = size;
        this.weight = weight;
    }
}
