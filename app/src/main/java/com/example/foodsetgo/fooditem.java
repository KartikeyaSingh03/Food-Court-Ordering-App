package com.example.foodsetgo;

public class fooditem {
    String price;

    public fooditem(String price, String status) {
        this.price = price;
        this.status = status;
    }

    String status;

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}
