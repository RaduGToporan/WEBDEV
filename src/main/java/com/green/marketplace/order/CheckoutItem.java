package com.green.marketplace.order;

public class CheckoutItem {
    private String id;
    private String name;
    private float unitPrice = 0;
    private int quantity = 0;
    private float totalPrice;

    public void setId(String id) {this.id = id;}
    public void setName(String name) {this.name = name;}

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
        this.totalPrice = this.unitPrice * quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = unitPrice * this.quantity;
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public float getUnitPrice() {return unitPrice;}
    public int getQuantity() {return quantity;}
    public float getTotalPrice() {return totalPrice;}
}
