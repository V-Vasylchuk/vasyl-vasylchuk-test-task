package org.example.model.product;

import java.time.LocalDate;

public class ProductFactory {
    public static RealProduct createRealProduct(String name, double price, int size, int weight) {
        return new RealProduct(name, price, size, weight);
    }

    public static VirtualProduct createVirtualProduct(String name, double price,
                                                      String code, LocalDate expirationDate) {
        return new VirtualProduct(name, price, code, expirationDate);
    }
}
