package com.green.marketplace.order;

// This class allows the cart items to be passed easily from backend to frontend
public class CartItem {
    private String key;
    private String name;
    private double unitPrice;
    private int quantity;
    private double price;

    CartItem(String key, String name, double unitPrice, int quantity) {
        this.key = key;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.price = unitPrice * quantity;
    }

    public String getKey() {return key;}
    public String getName() {return name;}
    public double getUnitPrice() {return unitPrice;}
    public int getQuantity() {return quantity;}
    public double getPrice() {return price;}

    public void setKey(String key) {this.key = key;}
    public void setName(String name) {this.name = name;}
    public void setUnitPrice(double unitPrice) {this.unitPrice = unitPrice;}
    public void setQuantity(int quantity) {this.quantity = quantity;}
    public void setPrice(double price) {this.price = price;}
}
