package com.example.foodsetgo;

public class User {
    String name;
    String password;
    String contact;
    String address;

    public User(String name, String password, String contact, String address) {
        this.name = name;
        this.password = password;
        this.contact = contact;
        this.address = address;
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
}
