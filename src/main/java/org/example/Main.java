package org.example;

import org.example.models.Order;
import org.example.models.OrderItem;
import org.example.models.Product;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Product product1 = new Product("3829172", "Кровать", "белый", 15600, 30);
        Product product2 = new Product("3829173", "Диван", 25600, 7);
        Product product3 = new Product("3829174", "Шкаф", "красный", 7800, 9);

        OrderItem item1 = new OrderItem(product1, 3);
        OrderItem item2 = new OrderItem(product2, 1);
        OrderItem item3 = new OrderItem(product3, 7);

        Order order = new Order("Николай Иванов", "(921)051-29-33",
                null, "Главный пр. 21-21", List.of(item1, item2, item3));

        DatabaseManagement.addProducts(product1, product2, product3);
        DatabaseManagement.printProductsList();
        DatabaseManagement.addOrder(order);
        try {
            DatabaseManagement.printOrderProducts(order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}