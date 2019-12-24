package com.example.foodsetgo;

import android.os.Bundle;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Cart extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.Adapter adap;
    RecyclerView.LayoutManager layoutManager;
    TextView tv;
    int GrandTotal = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        GlobalCart gc = ((GlobalCart)Cart.this.getApplicationContext());
        List<Pair<fooditem,String>> cart =  gc.getCART();
        tv = findViewById(R.id.grandtotal);
        Bundle bundle = getIntent().getExtras();
        final String uid = bundle.getString("UID");

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.rview_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Cart.this);
        recyclerView.setLayoutManager(layoutManager);
        adap = new CartAdapter(cart,Cart.this,uid);

        for(int i=0;i<cart.size();i++)
        {
            Pair<fooditem,String> food = cart.get(i);
            GrandTotal+=(Integer.parseInt(food.first.getPrice()))*(Integer.parseInt(food.second));
        }
        tv.setText("GrandTotal: "+GrandTotal+"+GST");
        recyclerView.setAdapter(adap);



    }
}
