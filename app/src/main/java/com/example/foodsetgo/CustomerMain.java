package com.example.foodsetgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.File;import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerMain extends AppCompatActivity {

    private Button signup;
    private Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        signin= findViewById(R.id.signin);
        signup= findViewById(R.id.signup);
        signin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                opensignin();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensignup();
            }
        });


    }
    public void opensignin()
    {
        Intent i=new Intent(CustomerMain.this, login.class);
        startActivity(i);
    }
    public void opensignup()
    {
        Intent i=new Intent(CustomerMain.this, signup.class);
        startActivity(i);
    }


}
