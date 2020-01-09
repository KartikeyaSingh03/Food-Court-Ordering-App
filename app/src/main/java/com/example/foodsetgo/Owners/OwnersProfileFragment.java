package com.example.foodsetgo.Owners;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodsetgo.EditDetails;
import com.example.foodsetgo.MainActivity;
import com.example.foodsetgo.R;
import com.example.foodsetgo.SharedPreferencesApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class OwnersProfileFragment extends Fragment {

    private View view;
    TextView RestName,OwnerEmail,Contact,address;
    Button edit,logout,addPhoto;
    String email,uid;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference root=FirebaseDatabase.getInstance().getReference();
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=   inflater.inflate(R.layout.fragment_owners_profile,container,false);
         RestName=view.findViewById(R.id.res_name);
         OwnerEmail=view.findViewById(R.id.owners_email_address);
         Contact=view.findViewById(R.id.res_number);
         address=view.findViewById(R.id.res_address);
         imageView=view.findViewById(R.id.profileImg);
         edit=view.findViewById(R.id.edit_button);
         logout=view.findViewById(R.id.logout_button);
        addPhoto=view.findViewById(R.id.add_photo);
         uid=firebaseAuth.getCurrentUser().getUid();
         email=firebaseAuth.getCurrentUser().getEmail();
        root=root.child("Restaurants").child(uid);

        OwnerEmail.setText(email);
        StorageReference storageRef=FirebaseStorage.getInstance().getReferenceFromUrl("gs://foodsetgo-120b6.appspot.com");

        storageRef.child("images/"+uid+'/'+"Restaurant_Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                final String strurl=uri.toString();
                Picasso.get().load(strurl).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });

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
                //startActivity(new Intent(getContext(), MainActivity.class));
                FirebaseAuth.getInstance().signOut();
                Intent in = new Intent(getContext(),MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                Toast.makeText(getContext(),"Logged Out Successfully",Toast.LENGTH_LONG).show();
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),AddProfilePhoto.class);
                startActivity(i);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getContext(), EditDetailsOwner.class);
                startActivity(i);
            }
        });


         return view;
    }




}
