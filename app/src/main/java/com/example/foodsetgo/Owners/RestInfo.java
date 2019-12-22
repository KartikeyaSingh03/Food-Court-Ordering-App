package com.example.foodsetgo.Owners;

public class RestInfo {
    private String name;
    private String contact;
    private String address;
    private String photoURL;
    private String restUid;
    public RestInfo()
    {

    }

    public RestInfo(String name, String contact, String address, String photoURL, String restUid) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.photoURL = photoURL;
        this.restUid = restUid;
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

    public String getRestUid() {
        return restUid;
    }
}