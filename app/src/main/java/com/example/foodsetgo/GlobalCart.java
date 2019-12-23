package com.example.foodsetgo;

import android.app.Application;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GlobalCart extends Application {
    private List<Pair<String,String>> CART = new ArrayList<>();

    public List<Pair<String, String>> getCART() {
        return CART;
    }

    public void setCART(List<Pair<String, String>> CART) {
        this.CART = CART;
    }
}
