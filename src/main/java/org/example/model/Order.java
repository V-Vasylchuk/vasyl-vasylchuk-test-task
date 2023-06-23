package org.example.model;

import java.util.List;
import lombok.Data;
import org.example.model.product.Product;

@Data
public class Order {
    private User user;
    private List<Product> products;

    private Order(User user, List<Product> products) {
        this.user = user;
        this.products = products;
    }

    public static Order createOrder(User user, List<Product> products) {
        return new Order(user, products);
    }
    /*private List<Double> prices;

    public static Order createOrder(User user, List<Product> products) {
        Order order = new Order();
        order.setUser(user);
        order.setPrices(products.stream()
                                .map(Product::getPrice)
                                .collect(Collectors.toList()));
        return order;
    }*/
}
