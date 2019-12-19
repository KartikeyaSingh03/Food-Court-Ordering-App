package com.example.foodsetgo.Owners;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsetgo.R;

import java.security.MessageDigest;

public class RegistrationRestaurant extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText confirm_password;
    Button next;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_restaurant);
        username = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);
        confirm_password = findViewById(R.id.confirm_password);
        next = findViewById(R.id.register);
        progressDialog = new ProgressDialog(this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });


    }

    public void Next() {
        String temp_username = (username.getText().toString().trim());
        String temp_password = (password.getText().toString().trim());
        String temp_confirm = (confirm_password.getText().toString().trim());
        if (temp_username.isEmpty() == true)
            Toast.makeText(this, "Please Restaurant's Email!", Toast.LENGTH_LONG).show();
        else if (temp_password.isEmpty() == true)
            Toast.makeText(this, "PleasePassword!", Toast.LENGTH_LONG).show();
        else if (temp_confirm.isEmpty() == true)
            Toast.makeText(this, "Please Confirm Password!", Toast.LENGTH_LONG).show();
        else if (temp_confirm.equals(temp_password) == false) {
            Toast.makeText(this, "The passwords do not match!", Toast.LENGTH_LONG).show();
        } else {
            moveToNext();
        }
    }

    public void moveToNext() {
        String temp_username = "", temp_password = "";
        Intent i = new Intent(RegistrationRestaurant.this, RegistrationRestaurant1.class);
        temp_username = (username.getText().toString().trim());
        i.putExtra("username", temp_username);
        temp_password = (password.getText().toString().trim());
        i.putExtra("password", temp_password);
        finish();
        startActivity(i);
    }

}

