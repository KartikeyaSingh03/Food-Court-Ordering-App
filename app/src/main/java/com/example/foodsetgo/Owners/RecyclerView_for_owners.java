package com.example.foodsetgo.Owners;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.R;
import com.example.foodsetgo.foodadapter;
import com.example.foodsetgo.fooditem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView_for_owners extends AppCompatActivity {

    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_for_owners);
        rv =findViewById(R.id.rview);
        rv.setHasFixedSize(true);
        Bundle bundle=getIntent().getExtras();
        final String username=bundle.getString("username");

        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        final List<fooditem> listmenus= new ArrayList<>();


        DatabaseReference root=FirebaseDatabase.getInstance().getReference();
        root.child("Restaurants").child(username).child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()==true)
                {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        fooditem f=dataSnapshot1.getValue(fooditem.class);
                        listmenus.add(f);
                    }
                    adap =new foodadapter(listmenus, RecyclerView_for_owners.this);
                    rv.setAdapter(adap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
