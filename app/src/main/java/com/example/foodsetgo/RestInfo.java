package com.example.foodsetgo;

import java.util.ArrayList;
import java.util.List;

public class RestInfo {
    private String name;
    private String password;
    private String contact;
    private String address;
    private ArrayList<fooditem> menu;

    public RestInfo(String name, String password, String contact, String address, ArrayList<fooditem> menu) {
        this.name = name;
        this.password = password;
        this.contact = contact;
        this.address = address;
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<fooditem> getMenu() {
        return menu;
    }
}