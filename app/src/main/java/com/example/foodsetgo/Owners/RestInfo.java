package com.example.foodsetgo.Owners;

public class RestInfo {
    private String name;
    private String contact;
    private String address;
    private String photoURL;

    public RestInfo(String name, String contact, String address,String url) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.photoURL= url;
    }

    public String getName() {
        return name;
    }


    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}