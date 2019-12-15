package com.example.foodsetgo.Owners;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsetgo.R;
import com.example.foodsetgo.SharedPreferencesApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class owners_options extends AppCompatActivity {

    Button additem,profile,signout,menulist;
    TextView name;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getIntent().getExtras();

        setContentView(R.layout.activity_owners_options);
        additem=findViewById(R.id.additem);
        profile=findViewById(R.id.myprofile);
        signout=findViewById(R.id.signout);
        menulist=findViewById(R.id.menulist);
        name=findViewById(R.id.restaurant_name);

        DatabaseReference root=database.getReference();

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(owners_options.this,additem.class);
                startActivity(i);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(owners_options.this,OwnerMain.class);
                Toast.makeText(owners_options.this,"Succesfully Looged Out",Toast.LENGTH_LONG).show();
                SharedPreferencesApp.setSessionState(owners_options.this,"NULL");
                startActivity(i);
            }
        });

        menulist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(owners_options.this, RecyclerView_for_owners.class);
                startActivity(i);
            }
        });
    }

}
