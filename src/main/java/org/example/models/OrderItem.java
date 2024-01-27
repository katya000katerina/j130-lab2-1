package org.example.models;

public class OrderItem {
    private final Product product;
    private final int price; //price at the time of the order
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
        price = product.getPrice();
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
