package com.example.foodsetgo;

import android.app.Application;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GlobalCart extends Application {
    private List<Pair<fooditem,String>> CART = new ArrayList<>();

    public List<Pair<fooditem, String>> getCART() {
        return CART;
    }

    public void setCART(List<Pair<fooditem, String>> CART) {
        this.CART = CART;
    }
}
