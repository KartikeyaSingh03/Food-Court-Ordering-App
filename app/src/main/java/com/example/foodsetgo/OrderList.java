package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderList extends AppCompatActivity {
    RecyclerView recyclerView ;
    RecyclerView.Adapter adap;
    RecyclerView.LayoutManager layoutManager;
    String UserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerView=findViewById(R.id.rview_for_foodtrack);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(OrderList.this);
        recyclerView.setLayoutManager(layoutManager);
        Bundle bundle = getIntent().getExtras();
        final String uid = bundle.getString("UID");
        String OrderNumber = bundle.getString("OrderNumber");
        final GoogleSignInAccount acct =  GoogleSignIn.getLastSignedInAccount(OrderList.this);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
            UserUid = firebaseAuth.getCurrentUser().getUid();
        if(acct!=null)
            UserUid= acct.getId();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root = root.child("Users/"+UserUid+"/orders/"+OrderNumber+"/OrderTray");
        final List<Pair<fooditem,String>> list = new ArrayList<>();
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int)dataSnapshot.getChildrenCount();
                for(int i=0;i<size;i++)
                {
                    DataSnapshot dataSnapshot1 = dataSnapshot.child(Integer.toString(i));
                    DataSnapshot d2 = dataSnapshot.child(Integer.toString(i)).child("first");
                    fooditem temp = new fooditem(d2.child("name").getValue(String.class),d2.child("price").getValue(String.class),d2.child("status").getValue(String.class),"",d2.child("username").getValue(String.class));
                    Pair<fooditem,String> food = Pair.create(temp,dataSnapshot1.child("second").getValue(String.class));
                    list.add(food);

                }
                Collections.reverse(list);
                adap = new OrderListAdapter(list,OrderList.this,uid);
                recyclerView.setAdapter(adap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
