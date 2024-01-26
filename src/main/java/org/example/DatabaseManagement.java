package org.example;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DatabaseManagement {
    private static final String url = "jdbc:mysql://localhost:3306/j_130_1";
    private static final String user = "root";
    private static final String password = "root";

    public static void addProducts(Product... products) {
        Set<Product> productsSet = new HashSet<>(List.of(products));
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement("insert into products values (?, ?, ?, ?, ?)")) {
            for (Product product : productsSet) {
                ps.setString(1, product.getVendorCode());
                ps.setString(2, product.getItemName());
                ps.setString(3, product.getColour());
                ps.setInt(4, product.getPrice());
                ps.setInt(5, product.getStockBalance());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printProductsList() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select * from products")) {
            while (rs.next()) {
                StringBuilder builder = new StringBuilder();
                builder.append(rs.getString("vendor_code"));
                builder.append(";");
                builder.append(rs.getString("item_name"));
                builder.append(";");
                builder.append(rs.getString("colour"));
                builder.append(";");
                builder.append(rs.getString("price"));
                builder.append(";");
                builder.append(rs.getString("stock_balance"));
                System.out.println(builder);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printOrderProducts(int orderId) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select p.item_name, p.colour from j_130_1.orders o " +
                     "join j_130_1.order_items oi on o.id = oi.order_id " +
                     "join j_130_1.products p on p.vendor_code = oi.product_vendor_code " +
                     "where o.id = " + orderId)) {
            System.out.printf("Список товаров заказа с id = %d:%n", orderId);
            while (rs.next()) {
                StringBuilder builder = new StringBuilder();
                builder.append(rs.getString("item_name"));
                String color = rs.getString("colour");
                if (color != null) {
                    builder.append(";");
                    builder.append(color);
                }
                System.out.println(builder);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addOrder(String customerFullName, String contactTelephoneNumber, String emailAddress, String deliveryAddress, Product... products) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement("insert into orders (creation_date, customer_full_name, contact_telephone_number, " +
                     "email_address, delivery_address, order_status, delivery_date) " +
                     " values(?, ?, ?, ?, ?, 'P', null);", Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(java.time.LocalDate.now()));
            ps.setString(2, customerFullName);
            ps.setString(3, contactTelephoneNumber);
            ps.setString(4, emailAddress);
            ps.setString(5, deliveryAddress);
            ps.execute();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    addProductsToOrder((int) generatedKeys.getLong(1), products);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addProductsToOrder(int orderId, Product... products) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps2 = connection.prepareStatement("insert into order_items values (?, ?, ?, ?);")) {
            for (Map.Entry<Product, Integer> entry : countProducts(products).entrySet()) {
                ps2.setInt(1, orderId);
                ps2.setString(2, entry.getKey().getVendorCode());
                ps2.setInt(3, entry.getKey().getPrice());
                ps2.setInt(4, entry.getValue());
                ps2.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Product, Integer> countProducts(Product... products) {
        Map<Product, Integer> map = new HashMap<>();
        for (Product product : products) {
            if (map.containsKey(product)) {
                map.put(product, map.get(product) + 1);
            } else {
                map.put(product, 1);
            }
        }
        return map;
    }
}