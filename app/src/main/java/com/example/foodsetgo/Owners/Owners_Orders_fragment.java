package com.example.foodsetgo.Owners;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Owners_Orders_fragment extends Fragment {

    View viewroot;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String OwnerUid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         viewroot=inflater.inflate(R.layout.fragment_owner_orders,container,false);
         recyclerView = viewroot.findViewById(R.id.rview_for_owners_orders);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        OwnerUid = firebaseAuth.getCurrentUser().getUid();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final List<Pair<String,Pair<String,String>>> listUsers = new ArrayList<>();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root = root.child("Restaurants").child(OwnerUid);
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("orders").exists())
                {
                    DataSnapshot d = dataSnapshot.child("orders");
                    int size = (int)d.getChildrenCount();
                    for(int i=1;i<=size;i++)
                    {
                        Pair<String,String> p = Pair.create(d.child(Integer.toString(i)).child("OrderNumUser").getValue().toString(),Integer.toString(i));
                        Pair<String,Pair<String,String>> user = Pair.create(d.child(Integer.toString(i)).child("CustUid").getValue().toString(),p);
                        listUsers.add(user);
                    }
                    adapter = new Owners_Orders_list_adapter(listUsers,getContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return viewroot;
    }
}
