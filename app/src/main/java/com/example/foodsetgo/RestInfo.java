package com.example.foodsetgo;

public class RestInfo {
    private String name;
    private String password;
    private String contact;
    private String address;
    private String menu;

    public RestInfo(String name, String password, String contact, String address, String menu) {
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

    public String getMenu() {
        return menu;
    }
}