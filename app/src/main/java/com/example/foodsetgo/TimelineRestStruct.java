package com.example.foodsetgo;

public class TimelineRestStruct {
    String uid;
    String GrandTotal;
    String status;
    String orderNumber;

    public TimelineRestStruct(String uid, String grandTotal, String status,String orderNumber) {
        this.uid = uid;
        this.GrandTotal = grandTotal;
        this.status = status;
        this.orderNumber = orderNumber;
    }
    public TimelineRestStruct()
    {

    }

    public String getUid() {
        return uid;
    }

    public String getGrandTotal() {
        return GrandTotal;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}
