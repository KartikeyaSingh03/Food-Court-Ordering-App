package com.example.foodsetgo;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class additem extends AppCompatActivity {
    Spinner spinner;
    EditText foodname,price;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        final Bundle bundle = getIntent().getExtras();
        spinner=findViewById(R.id.spinner);
        foodname=findViewById(R.id.foodname);
        price=findViewById(R.id.price);
        add=findViewById(R.id.additem);

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(additem.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.status));


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String text=spinner.getSelectedItem().toString().trim();

                final String itemname=foodname.getText().toString().trim();
                final String itemprice=price.getText().toString().trim();
                final String username=bundle.getString("username").trim();
                final fooditem temp = new fooditem(itemname,itemprice, text,"");

                DatabaseReference root=FirebaseDatabase.getInstance().getReference();
                   root.child("Restaurants").child(username).child("menu").child(temp.getName()).child("Name").setValue(temp.getName() );
                   root.child("Restaurants").child(username).child("menu").child(temp.getName()).child("Price").setValue(temp.getPrice());
                   root.child("Restaurants").child(username).child("menu").child(temp.getName()).child("Status").setValue(temp.getStatus());



            }
        });


    }
}
