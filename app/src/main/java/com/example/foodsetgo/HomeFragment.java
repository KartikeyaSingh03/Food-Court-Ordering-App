package com.example.foodsetgo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.Owners.RestInfo;
import com.example.foodsetgo.Owners.additem;
import com.example.foodsetgo.Owners.foodadapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adap;
    private View viewroot;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewroot=inflater.inflate(R.layout.fragment_home,container,false);
        rv=viewroot.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        final List<RestInfo> listRests= new ArrayList<>();


        DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        root.child("Restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()==true)
                {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        RestInfo f=dataSnapshot1.getValue(RestInfo.class);
                        listRests.add(f);
                    }
                    adap =new RestaurantListAdapter(listRests, getContext());
                    rv.setAdapter(adap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return viewroot;
    }
}
