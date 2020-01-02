package com.example.foodsetgo;

public class PaytmOrder extends com.paytm.pgsdk.PaytmOrder {
    String orderid;
    String mid;
    String txnToken;
    String amount;
    String callbackurl;

    public PaytmOrder() {
        super(null);
    }

    public PaytmOrder(String orderid, String mid, String txnToken, String amount, String callbackurl) {
        super(null);
        this.orderid = orderid;
        this.mid = mid;
        this.txnToken = txnToken;
        this.amount = amount;
        this.callbackurl = callbackurl;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTxnToken() {
        return txnToken;
    }

    public void setTxnToken(String txnToken) {
        this.txnToken = txnToken;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }
}
