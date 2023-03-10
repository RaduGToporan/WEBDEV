package com.green.marketplace.order;

public class OrderItem {
    private String name;
    private double unitPrice;
    private int quantity;
    private double price;

    public String getName() {return name;}
    public double getUnitPrice() {return unitPrice;}
    public int getQuantity() {return quantity;}
    public double getPrice() {return price;}

    public void setName(String name) {this.name = name;}
    public void setUnitPrice(double unitPrice) {this.unitPrice = unitPrice;}
    public void setQuantity(int quantity) {this.quantity = quantity;}
    public void setPrice(double price) {this.price = price;}

}
