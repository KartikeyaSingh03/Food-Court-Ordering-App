package com.example.foodsetgo;

import android.os.Bundle;
import android.util.Pair;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Cart extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.Adapter adap;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        GlobalCart gc = ((GlobalCart)Cart.this.getApplicationContext());
        List<Pair<String,String>> cart =  gc.getCART();
        recyclerView = findViewById(R.id.rview_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Cart.this);
        recyclerView.setLayoutManager(layoutManager);
        Bundle bundle = getIntent().getExtras();
        adap = new CartAdapter(cart,Cart.this,bundle.getString("UID"));
        recyclerView.setAdapter(adap);
    }
}
