package com.example.foodsetgo;

public class fooditem {
    private String price;
    private String status;

    public fooditem(String price, String status) {
        this.price = price;
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
}
