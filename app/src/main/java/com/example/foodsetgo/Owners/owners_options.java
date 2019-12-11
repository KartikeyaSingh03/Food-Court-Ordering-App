package com.example.foodsetgo.Owners;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsetgo.R;
import com.example.foodsetgo.SharedPreferenceForOwner;

public class owners_options extends AppCompatActivity {

    Button additem,profile,signout,menulist;
    TextView name;


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
        name.setText(bundle.getString("name"));
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(owners_options.this,additem.class);
                final String temp_name=name.getText().toString().trim();
                i.putExtra("username",bundle.getString("username"));
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
                SharedPreferenceForOwner.clearUserName(owners_options.this);
                Intent i=new Intent(owners_options.this,OwnerMain.class);
                Toast.makeText(owners_options.this,"Succesfully Looged Out",Toast.LENGTH_LONG).show();
                startActivity(i);
                finish();
            }
        });

        menulist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(owners_options.this, RecyclerView_for_owners.class);
                i.putExtra("username",bundle.getString("username"));

                startActivity(i);
            }
        });
    }

}
