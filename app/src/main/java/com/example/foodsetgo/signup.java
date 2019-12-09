package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText confirm_password;
    Button   next;
    int flag;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username= findViewById(R.id.edit_email);
        password= findViewById(R.id.edit_password);
        confirm_password= findViewById(R.id.confirm_password);
        next= findViewById(R.id.register);
        progressDialog=new ProgressDialog(this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });


    }

    public void Next()
    {
        String temp_username=username.getText().toString().trim();
        String temp_password=password.getText().toString().trim();
        String temp_confirm=confirm_password.getText().toString().trim();
        if(temp_username.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Email!",Toast.LENGTH_LONG).show();
        else
        if(temp_password.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Password!",Toast.LENGTH_LONG).show();
        else
        if(temp_confirm.isEmpty()==true)
            Toast.makeText(this,"Please Confirm Your Password!",Toast.LENGTH_LONG).show();
        else if(temp_confirm.equals(temp_password)==false) {
            Toast.makeText(this, "The passwords do not match!", Toast.LENGTH_LONG).show();
        }
        else if(isValidEmail(temp_username)==false||EmailChar(temp_username)==false)
            Toast.makeText(this, "Email ID is invalid", Toast.LENGTH_LONG).show();
        else if(passStrength(temp_password)==false)
           Toast.makeText(this, "Password must be at least 8 characters long, must contain a letter[a-z,A-Z], and a number[0-9]", Toast.LENGTH_LONG).show();
        else
        {
            final String tusername=temp_username.trim();
            moveToNext();
        }
    }

    public void moveToNext(){
        String temp_username,temp_password;
        Intent i= new Intent(signup.this,SignUp1.class);
        temp_username = username.getText().toString().trim();
        i.putExtra("username", temp_username);
        temp_password=password.getText().toString().trim();
        i.putExtra("password",temp_password);
        startActivity(i);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean EmailChar(String s){
        for(int i=0;i<s.length();i++){
            char c =s.charAt(i);
            if(Character.isLetter(c)==false&&Character.isDigit(c)==false&&c!='.'&&c!='_'&&c!='@')
                return false;
        }
        return true;
    }

    public static Boolean passStrength(String password){
        boolean hasLetter = false;
        boolean hasDigit = false;
        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                char x = password.charAt(i);
                if (Character.isLetter(x)) {
                    hasLetter = true;
                }
                else if (Character.isDigit(x)) {
                    hasDigit = true;
                }
            }
            if(hasLetter && hasDigit){
                return true;
            }
        }
        return false;
    }





    // method to check if user exists.
    public void checklogin(final String temp_username, final String temp_password)
    {
        DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        root.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()==true)
                {
                    if(dataSnapshot.child(temp_username).exists()==true)
                    {
                        flag=1;
                    }
                    else
                    {
                        flag=0;
                    }
                }
                else
                    flag=0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    }