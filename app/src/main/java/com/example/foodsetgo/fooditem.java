package com.example.foodsetgo;

public class fooditem {
    private String Name;
    private String Price;
    private String Status;
    private String ImgUrl;
    private String Username;

    public fooditem()
    {}

    public fooditem(String name, String price, String status, String imgUrl, String username) {
        Name = name;
        Price = price;
        Status = status;
        ImgUrl = imgUrl;
        Username = username;
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

    public String getImgUrl() {
        return ImgUrl;
    }

    public String getUsername() {
        return Username;
    }
}


