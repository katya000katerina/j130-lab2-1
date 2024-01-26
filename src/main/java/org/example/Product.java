package org.example;

import java.util.Objects;

public class Product {
    private final String vendorCode;
    private String itemName;
    private String colour;
    private int price;
    private int stockBalance;

    public Product(String vendorCode, String itemName, int price, int stockBalance) {
        if (vendorCode == null || vendorCode.length() != 7) {
            throw new IllegalArgumentException("Vendor code should consist of 7 characters");
        }
        this.vendorCode = vendorCode;
        setItemName(itemName);
        setPrice(price);
        setStockBalance(stockBalance);
    }
    public Product(String vendorCode, String itemName, String colour, int price, int stockBalance) {
        this(vendorCode, itemName, price, stockBalance);
        setColour(colour);
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        if (itemName == null || itemName.length() > 50) {
            throw new IllegalArgumentException("Item name should consist of 50 characters or less");
        }
        this.itemName = itemName;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        if (colour == null || colour.length() > 20) {
            throw new IllegalArgumentException("Colour should consist of 50 characters or less");
        }
        this.colour = colour;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price should be greater then zero");
        }
        this.price = price;
    }

    public int getStockBalance() {
        return stockBalance;
    }

    public void setStockBalance(int stockBalance) {
        if (stockBalance < 0) {
            throw new IllegalArgumentException("Stock balance cannot be a negative number");
        }
        this.stockBalance = stockBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(vendorCode, product.vendorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorCode);
    }
}
