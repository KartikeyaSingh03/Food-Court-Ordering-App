package com.example.foodsetgo;

public class fooditem {
    private String Name;
    private String Price;
    private String Status;

    public fooditem(String name, String price, String status) {
        Name = name;
        Price = price;
        Status = status;
    }
    public fooditem()
    {

    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public String getStatus() {
        return Status;
    }
}


