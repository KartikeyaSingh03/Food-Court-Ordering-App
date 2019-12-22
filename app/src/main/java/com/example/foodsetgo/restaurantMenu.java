package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodsetgo.Owners.RestInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class restaurantMenu extends AppCompatActivity {
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adap;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        Bundle bundle =getIntent().getExtras();
        uid=bundle.getString("UID");
        rv=findViewById(R.id.rview_menu);
        rv.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(restaurantMenu.this);
        rv.setLayoutManager(layoutManager);
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
    }
}
