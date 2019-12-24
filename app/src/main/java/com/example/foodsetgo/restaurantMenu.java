package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.example.foodsetgo.Owners.RestInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class restaurantMenu extends AppCompatActivity {
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adap;
    private HashMap<String,ArrayList<Pair<String,String>>> CART;
    private Button viewcart;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        viewcart = findViewById(R.id.view_cart);
        Bundle bundle =getIntent().getExtras();
        uid=bundle.getString("UID");
        rv=findViewById(R.id.rview_menu);
        rv.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(restaurantMenu.this);
        rv.setLayoutManager(layoutManager);
        GlobalCart gc = (GlobalCart)restaurantMenu.this.getApplicationContext();
        final List<Pair<fooditem,String>> cart = gc.getCART();
        cart.clear();
        gc.setCART(cart);


        final List<fooditem> listmenu= new ArrayList<>();

        DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        root.child("Restaurants").child(uid).child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()==true)
                {

                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        fooditem f=dataSnapshot1.getValue(fooditem.class);
                        listmenu.add(f);
                    }
                    adap =new MenuAdapter(listmenu,restaurantMenu.this,uid);
                    rv.setAdapter(adap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(restaurantMenu.this,Cart.class);
                i.putExtra("UID",uid);
                startActivity(i);
            }
        });
    }

    public HashMap<String, ArrayList<Pair<String, String>>> getCART() {
        return CART;
    }

    public void setCART(HashMap<String, ArrayList<Pair<String, String>>> CART) {
        this.CART = CART;
    }


}
