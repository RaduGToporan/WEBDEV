package com.green.marketplace.order;

import java.util.List;

public class PastOrder {
    private int orderNum;
    private String user;
    private String date;
    private String to;
    private String status;
    private List<OrderItem> items;

    public void setOrderNum(int orderNum) {this.orderNum = orderNum;}
    public void setUser(String user) {this.user = user;}
    public void setDate(String date) {this.date = date;}
    public void setTo(String to) {this.to = to;}
    public void setStatus(String status) {this.status = status;}
    public void setItems(List<OrderItem> items) {this.items = items;}

    public int getOrderNum() {return orderNum;}
    public String getUser() {return user;}
    public String getDate() {return date;}
    public String getTo() {return to;}
    public String getStatus() {return status;}
    public List<OrderItem> getItems() {return items;}
}