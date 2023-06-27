package org.example;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.example.exception.DataException;
import org.example.model.Order;
import org.example.model.User;
import org.example.model.product.Product;
import org.example.model.product.ProductFactory;
import org.example.model.product.RealProduct;

public class Main {
    public static void main(String[] args) {
        User user1 = User.createUser("Alice", 32);
        User user2 = User.createUser("Bob", 19);
        User user3 = User.createUser("Charlie", 20);
        User user4 = User.createUser("John", 27);

        Product realProduct1 = ProductFactory
                .createRealProduct("Product A", 20.50, 10, 25);
        Product realProduct2 = ProductFactory
                .createRealProduct("Product B", 50, 6, 17);

        Product virtualProduct1 = ProductFactory.createVirtualProduct("Product C",
                100, "xxx", LocalDate.of(2023, 5, 12));
        Product virtualProduct2 = ProductFactory.createVirtualProduct("Product D",
                81.25, "yyy", LocalDate.of(2024, 6, 20));

        List<Order> orders = new ArrayList<>() {
            {
                add(Order.createOrder(user1, List.of(realProduct1, virtualProduct1,
                                                    virtualProduct2)));
                add(Order.createOrder(user2, List.of(realProduct1, realProduct2)));
                add(Order.createOrder(user3, List.of(realProduct1, virtualProduct2)));
                add(Order.createOrder(user4, List.of(virtualProduct1, virtualProduct2,
                                                    realProduct1, realProduct2)));
            }
        };

        System.out.println("1. Create singleton class VirtualProductCodeManager \n");
        var isUsed = false;
        System.out.println("Is code used: " + isUsed + "\n");

        Product mostExpensive = getMostExpensiveProduct(orders);
        System.out.println("2. Most expensive product: " + mostExpensive + "\n");

        Product mostPopular = getMostPopularProduct(orders);
        System.out.println("3. Most popular product: " + mostPopular + "\n");

        double averageAge = calculateAverageAge(realProduct2, orders);
        System.out.println("4. Average age is: " + averageAge + "\n");

        Map<Product, List<User>> productUserMap = getProductUserMap(orders);
        System.out.println("5. Map with products as keys and list of users as value \n");
        productUserMap.forEach((key, value) -> System.out.println("key: " + key + " "
                + "value: " + value + "\n"));

        List<Product> productsByPrice = sortProductsByPrice(List.of(realProduct1, realProduct2,
                virtualProduct1, virtualProduct2));
        System.out.println("6. a) List of products sorted by price: " + productsByPrice + "\n");
        List<Order> ordersByUserAgeDesc = sortOrdersByUserAgeDesc(orders);
        System.out.println("6. b) List of orders sorted by user age in descending order: "
                + ordersByUserAgeDesc + "\n");

        Map<Order, Integer> result = calculateWeightOfEachOrder(orders);
        System.out.println("7. Calculate the total weight of each order \n");
        result.forEach((key, value) -> System.out.println("order: " + key + " "
                + "total weight: " + value + "\n"));
    }

    private static Product getMostExpensiveProduct(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getProducts().stream())
                .max(Comparator.comparingDouble(Product::getPrice))
                .orElseThrow(() -> new DataException("Product not found"));
    }

    private static Product getMostPopularProduct(List<Order> orders) {
        final Map<Product, Long> productCountMap = orders.stream()
                .flatMap(order -> order.getProducts().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return productCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new DataException("Product not found"));
    }

    private static double calculateAverageAge(Product product, List<Order> orders) {
        final List<User> usersWhoBoughtProduct = orders.stream()
                .filter(order -> order.getProducts().contains(product))
                .map(Order::getUser)
                .toList();

        return usersWhoBoughtProduct.stream()
                .mapToDouble(User::getAge)
                .average()
                .orElseThrow(() -> new DataException("Can't calculate average age for users "
                        + "who bought: " + product));
    }

    private static Map<Product, List<User>> getProductUserMap(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getProducts().stream()
                        .map(product -> new AbstractMap.SimpleEntry<>(product, order.getUser())))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    private static List<Product> sortProductsByPrice(List<Product> products) {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .toList();
    }

    private static List<Order> sortOrdersByUserAgeDesc(List<Order> orders) {
        List<Order> sortedOrders = new ArrayList<>(orders);
        sortedOrders.sort(Comparator.comparingInt(order -> order.getUser().getAge()));
        Collections.reverse(sortedOrders);
        return sortedOrders;
    }

    private static Map<Order, Integer> calculateWeightOfEachOrder(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.toMap(
                        order -> order,
                        order -> order.getProducts().stream()
                                .filter(product -> product instanceof RealProduct)
                                .mapToInt(product -> ((RealProduct) product).getWeight())
                                .sum()
                ));
    }
}
