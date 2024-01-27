package org.example;

import org.example.models.Order;
import org.example.models.OrderItem;
import org.example.models.Product;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                String builder = rs.getString("vendor_code") +
                        ";" +
                        rs.getString("item_name") +
                        ";" +
                        rs.getString("colour") +
                        ";" +
                        rs.getString("price") +
                        ";" +
                        rs.getString("stock_balance");
                System.out.println(builder);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printOrderProducts(Order order) throws SQLException {
        int orderId = order.getId();
        if (orderId == 0) {
            throw new SQLException("The order has not been added to the database yet");
        }
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

    public static void addOrder(Order order) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = connection.prepareStatement("insert into orders (creation_date, customer_full_name, contact_telephone_number, " +
                     "email_address, delivery_address, order_status, delivery_date) " +
                     " values(?, ?, ?, ?, ?, 'P', null);", Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(java.time.LocalDate.now()));
            ps.setString(2, order.getCustomerFullName());
            ps.setString(3, order.getContactTelephoneNumber());
            ps.setString(4, order.getEmailAddress());
            ps.setString(5, order.getDeliveryAddress());
            ps.execute();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = (int) generatedKeys.getLong(1);
                    order.setId(id);
                    addProductsToOrder(id, order.getOrderItems());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addProductsToOrder(int orderId, List<OrderItem> items) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement ps2 = connection.prepareStatement("insert into order_items values (?, ?, ?, ?);")) {
            for (OrderItem orderItem : items) {
                ps2.setInt(1, orderId);
                ps2.setString(2, orderItem.getProduct().getVendorCode());
                ps2.setInt(3, orderItem.getPrice());
                ps2.setInt(4, orderItem.getQuantity());
                ps2.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}