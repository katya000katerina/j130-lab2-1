package org.example.models;

import org.example.enums.OrderStatus;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final Date creationDate;
    private int id;
    private String customerFullName;
    private String contactTelephoneNumber;
    private String emailAddress;
    private String deliveryAddress;
    private OrderStatus orderStatus;
    private Date deliveryDate;
    private List<OrderItem> orderItems;

    public Order(String customerFullName, String contactTelephoneNumber, String emailAddress, String deliveryAddress, List<OrderItem> orderItems) {
        creationDate = Date.valueOf(java.time.LocalDate.now());
        setCustomerFullName(customerFullName);
        setContactTelephoneNumber(contactTelephoneNumber);
        setEmailAddress(emailAddress);
        setDeliveryAddress(deliveryAddress);
        orderStatus = OrderStatus.P;
        setOrderItems(orderItems);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != 0) {
            return;
        }
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }


    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        if (customerFullName == null || customerFullName.length() > 100) {
            throw new IllegalArgumentException("Customer full name should consist of 100 characters or less");
        }
        this.customerFullName = customerFullName;
    }

    public String getContactTelephoneNumber() {
        return contactTelephoneNumber;
    }

    public void setContactTelephoneNumber(String contactTelephoneNumber) {
        if (contactTelephoneNumber != null && contactTelephoneNumber.length() > 50) {
            throw new IllegalArgumentException("Contact telephone number should consist of 50 characters or less or be null");
        }
        this.contactTelephoneNumber = contactTelephoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        if (emailAddress != null && emailAddress.length() > 50) {
            throw new IllegalArgumentException("Email address should consist of 50 characters or less or be null");
        }
        this.emailAddress = emailAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        if (deliveryAddress == null || deliveryAddress.length() > 200) {
            throw new IllegalArgumentException("Colour should consist of 200 characters or less");
        }
        this.deliveryAddress = deliveryAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void cancelOrder() {
        orderStatus = OrderStatus.C;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDateAndShippedStatus(Date deliveryDate) {
        if (deliveryDate == null) {
            throw new IllegalArgumentException("Delivery date cannot be null for shipped orders");
        }
        this.deliveryDate = deliveryDate;
        orderStatus = OrderStatus.S;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Every order should have products in it");
        }
        this.orderItems = orderItems;
    }
}
