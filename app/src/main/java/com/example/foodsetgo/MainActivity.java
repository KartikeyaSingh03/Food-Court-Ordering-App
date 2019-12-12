package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.security.acl.Owner;

public class MainActivity extends AppCompatActivity {
    Button customerBtn;
    Button ownerBtn;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customerBtn=(Button)findViewById(R.id.Customer);
        ownerBtn=(Button)findViewById(R.id.Owner);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), UserHome.class));
        }
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(MainActivity.this, UserHome.class));
        }
        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(MainActivity.this, CustomerMain.class);
                    startActivity(i);
                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ownerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String temp=SharedPreferenceForOwner.getUserName(MainActivity.this);
                    if(temp.isEmpty()==false)
                    {
                        final Intent i=new Intent(MainActivity.this, com.example.foodsetgo.Owners.owners_options.class);
                        i.putExtra("username",temp);


                        DatabaseReference root=FirebaseDatabase.getInstance().getReference().child("Restaurants").child(temp);
                        root.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null)
                                {
                                    Toast.makeText(MainActivity.this,"Welcome "+dataSnapshot.child("name").getValue(),Toast.LENGTH_LONG).show();
                                    startActivity(i);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else {
                        Intent i = new Intent(MainActivity.this, com.example.foodsetgo.Owners.OwnerMain.class);
                        startActivity(i);
                    }
                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
