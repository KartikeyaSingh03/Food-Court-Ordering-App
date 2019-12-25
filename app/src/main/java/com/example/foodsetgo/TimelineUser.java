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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TimelineUser extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adap;
    RecyclerView.LayoutManager layoutManager;
    String UserUid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        final View v= inflater.inflate(R.layout.timeline, container, false);

        recyclerView = v.findViewById(R.id.rview_for_timeline);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        final List<TimelineRestStruct> list = new ArrayList<>();
        final GoogleSignInAccount acct =  GoogleSignIn.getLastSignedInAccount(getContext());

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
            UserUid = firebaseAuth.getCurrentUser().getUid();
        if(acct!=null)
            UserUid= acct.getId();
        root = root.child("Users").child(UserUid).child("orders");
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int)dataSnapshot.getChildrenCount();

                for(int i=1;i<=size;i++)
                {
                    final String uid = dataSnapshot.child(Integer.toString(i)).child("UID").getValue(String.class);
                    final String Status = dataSnapshot.child(Integer.toString(i)).child("Status").getValue(String.class);
                    final String grandtotal = dataSnapshot.child(Integer.toString(i)).child("GrandTotal").getValue(String.class);
                    final TimelineRestStruct rest = new TimelineRestStruct(uid,Status,grandtotal,Integer.toString(i));
                    list.add(rest);
                }
                Collections.reverse(list);
                adap = new TimelineAdapter(list,getContext());
                recyclerView.setAdapter(adap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

}
