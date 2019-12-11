package com.example.foodsetgo.Owners;

public class RestInfo {
    private String name;
    private String password;
    private String contact;
    private String address;
    private String username;

    public RestInfo(){}

    public RestInfo(String name, String password, String contact, String address, String username) {
        this.name = name;
        this.password = password;
        this.contact = contact;
        this.address = address;
        this.username = username;
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

    public String getUsername() {
        return username;
    }
}