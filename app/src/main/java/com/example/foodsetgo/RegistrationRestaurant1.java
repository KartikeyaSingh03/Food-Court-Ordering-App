package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RegistrationRestaurant1 extends AppCompatActivity {
    EditText name;
    EditText address;
    EditText contact;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_restaurant1);
        name= findViewById(R.id.name);
        contact= findViewById(R.id.number);
        address= findViewById(R.id.address);
        register= findViewById(R.id.register);
        Bundle bundle = getIntent().getExtras();

        String temp_username=bundle.getString("username");
        final  String username=sha256(temp_username).trim();
        final String temp_password=bundle.getString("password");
        final String pass=sha256(temp_password).trim();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Register(username,pass);
            }
        });


    }

    public void Register(String username,String password)
    {
        final String temp_name=name.getText().toString().trim();
        final String temp_address=address.getText().toString().trim();
        final String temp_contact=contact.getText().toString().trim();
        if(temp_name.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Name!",Toast.LENGTH_LONG).show();
        else
        if(temp_address.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Address!",Toast.LENGTH_LONG).show();
        else
        if(temp_contact.isEmpty()==true||isValidContact(temp_contact)==false)
            Toast.makeText(this,"Please Enter a Valid Contact!",Toast.LENGTH_LONG).show();
        else
        {

            final ProgressDialog progress=new ProgressDialog(RegistrationRestaurant1.this);
            progress.setMessage("Registering...");
            progress.show();

            ArrayList<fooditem> menu=new ArrayList<fooditem>();

            RestInfo R=new RestInfo(temp_name,password,temp_contact,temp_address,menu);
            DatabaseReference root=FirebaseDatabase.getInstance().getReference();




            root.child("Restaurants").child(username).setValue(R)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()==true)
                            {
                                Toast.makeText(RegistrationRestaurant1.this,"Registration Successful",Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                            else
                            {
                                Toast.makeText(RegistrationRestaurant1.this,"Registration UnSuccessful",Toast.LENGTH_LONG).show();
                                progress.dismiss();

                            }
                        }
                    });
            name.setText("");
            address.setText("");
            contact.setText("");

        }

    }
    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean isValidContact(String s){
        if(s.length()==10)
            return true;
        else
            return false;
    }

}