package com.example.foodsetgo;

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

import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText confirm_password;
    Button   next;
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
        else
        {
            moveToNext();
        }
    }

    public void moveToNext(){
        String temp_username="",temp_password="";
        Intent i= new Intent(signup.this,SignUp1.class);
        temp_username = username.getText().toString().trim();
        i.putExtra("username", temp_username);
        temp_password=password.getText().toString().trim();
        i.putExtra("password",temp_password);
        startActivity(i);
    }

}