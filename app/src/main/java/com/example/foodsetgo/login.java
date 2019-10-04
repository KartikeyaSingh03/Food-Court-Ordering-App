package com.example.foodsetgo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {
    EditText username;
    EditText password;
    Button   login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.edit_email);
        password=(EditText)findViewById(R.id.edit_password);
        login=(Button)findViewById(R.id.login);

    }
}
