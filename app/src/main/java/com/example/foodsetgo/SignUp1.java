package com.example.foodsetgo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp1 extends AppCompatActivity {
    EditText name;
    EditText address;
    EditText contact;
    Button register;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);
        name= findViewById(R.id.name);
        contact= findViewById(R.id.number);
        address= findViewById(R.id.address);
        register= findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });


    }

    public void Register()
    {
        String temp_name=name.getText().toString();
        String temp_address=address.getText().toString();
        String temp_contact=contact.getText().toString();
        if(temp_name.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Name!",Toast.LENGTH_LONG).show();
        else
        if(temp_address.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Address!",Toast.LENGTH_LONG).show();
        else
        if(temp_contact.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Contact!",Toast.LENGTH_LONG).show();
        else
        {
            progressDialog.setMessage("Registering User...");
            progressDialog.show();

        }

    }
}
