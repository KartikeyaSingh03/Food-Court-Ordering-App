package com.example.foodsetgo.Owners;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodsetgo.MainActivity;
import com.example.foodsetgo.R;
import com.example.foodsetgo.SharedPreferencesApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OwnersProfileFragment extends Fragment {

    private View view;
    TextView RestName,OwnerEmail,Contact,address;
    Button edit,logout;
    String email,uid;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference root=FirebaseDatabase.getInstance().getReference();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=   inflater.inflate(R.layout.fragment_owners_profile,container,false);
         RestName=view.findViewById(R.id.res_name);
         OwnerEmail=view.findViewById(R.id.owners_email_address);
         Contact=view.findViewById(R.id.res_number);
         address=view.findViewById(R.id.res_address);

         edit=view.findViewById(R.id.edit_button);
         logout=view.findViewById(R.id.logout_button);

         uid=firebaseAuth.getCurrentUser().getUid();
         email=firebaseAuth.getCurrentUser().getEmail();
        root=root.child("Restaurants").child(uid);

        OwnerEmail.setText(email);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("name").exists())
                    {
                        RestName.setText(dataSnapshot.child("name").getValue().toString());

                    }
                    if(dataSnapshot.child("contact").exists())
                    {
                        Contact.setText(dataSnapshot.child("contact").getValue().toString());

                    }
                    if(dataSnapshot.child("address").exists())
                    {
                        address.setText(dataSnapshot.child("address").getValue().toString());

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                SharedPreferencesApp.setSessionState(getContext(),"NULL");
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });




         return view;
    }




}
