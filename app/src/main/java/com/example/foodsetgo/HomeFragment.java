package com.example.foodsetgo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrl = new ArrayList<>();
    View Viewroot;
    FirebaseDatabase database;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        Viewroot=inflater.inflate(R.layout.fragment_home, container, false);
        database = FirebaseDatabase.getInstance();
        initImageBitmaps();

        return Viewroot;
    }

    private void initImageBitmaps(){
        database.getReference().child("Restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String name,url;
                    if(snapshot.child("name").exists())
                        name = snapshot.child("name").getValue().toString();
                    else
                        name="Name Not Found";
                    if(snapshot.child("photoURL").exists())
                        url=  snapshot.child("photoURL").getValue().toString();
                    else
                        url="https://firebasestorage.googleapis.com/v0/b/foodsetgo-120b6.appspot.com/o/uploads%2Fic_launcher-web.png?alt=media&token=d838edb8-f5b4-4dc4-9d93-c9ab8f55eed6";
                    mImageUrl.add(url);
                    mNames.add(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = Viewroot.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mImageUrl,mNames,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
