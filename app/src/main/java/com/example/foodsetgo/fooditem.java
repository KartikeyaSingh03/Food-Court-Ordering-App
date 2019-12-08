package com.example.foodsetgo;

public class fooditem {
    private String Name;
    private String Price;
    private String Status;
    private String ImgUrl;


    public fooditem()
    {}
    public fooditem(String name, String price, String status, String imgUrl) {
        Name = name;
        Price = price;
        Status = status;
        ImgUrl = imgUrl;
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

}


