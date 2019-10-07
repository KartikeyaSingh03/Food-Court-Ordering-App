package com.example.foodsetgo;

public class foodinfo {
    private String foodname;
    private fooditem food;

    public foodinfo(String foodname, fooditem food) {
        this.foodname = foodname;
        this.food = food;
    }

    public String getFoodname() {
        return foodname;
    }

    public fooditem getFood() {
        return food;
    }
}
