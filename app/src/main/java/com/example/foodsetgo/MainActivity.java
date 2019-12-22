package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(MainActivity.this, UserHome.class));
        }
        else {
            if (SharedPreferencesApp.getSessionState(MainActivity.this).equals("NULL") == false) {

                if (SharedPreferencesApp.getSessionState(MainActivity.this).equals("User")) {
                    finish();
                    //opening profile activity
                    startActivity(new Intent(getApplicationContext(), UserHome.class));
                } else {
                    finish();
                    //opening owner's options activity
                    startActivity(new Intent(getApplicationContext(), com.example.foodsetgo.Owners.Res_Home.class));
                }

            }
        }

        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(MainActivity.this, CustomerMain.class);
                    finish();
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


                        Intent i = new Intent(MainActivity.this, com.example.foodsetgo.Owners.OwnerMain.class);
                        finish();
                        startActivity(i);

                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
