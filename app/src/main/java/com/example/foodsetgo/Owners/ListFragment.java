package com.example.foodsetgo.Owners;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.R;
import com.example.foodsetgo.fooditem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adap;
    private View viewroot;
    private Button button;
    String email,username;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           viewroot=inflater.inflate(R.layout.fragment_owners_list,container,false);

        rv=viewroot.findViewById(R.id.rview);
        rv.setHasFixedSize(true);
        FloatingActionButton button =viewroot.findViewById(R.id.fab);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(getContext(),additem.class));
            }
        });
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        final List<fooditem> listmenus= new ArrayList<>();

        email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        username=FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference root= FirebaseDatabase.getInstance().getReference();
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
                    adap =new foodadapter(listmenus, getContext());
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
