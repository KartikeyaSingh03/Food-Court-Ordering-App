package com.example.foodsetgo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    EditText name;
    EditText username;
    EditText password;
    EditText confirm_password;
    EditText address;
    EditText contact;
    Button   register;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name=(EditText)findViewById(R.id.name);
        username=(EditText)findViewById(R.id.edit_email);
        password=(EditText)findViewById(R.id.edit_password);
        confirm_password=(EditText)findViewById(R.id.confirm_password);
        contact=(EditText)findViewById(R.id.number);
        address=(EditText)findViewById(R.id.address);
        register=(Button)findViewById(R.id.register);
        progressDialog=new ProgressDialog(this);
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
        String temp_username=username.getText().toString();
        String temp_password=password.getText().toString();
        String temp_confirm=confirm_password.getText().toString();
        String temp_address=address.getText().toString();
        String temp_contact=contact.getText().toString();
        if(temp_name.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Name!",Toast.LENGTH_LONG).show();
        else
        if(temp_username.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Email!",Toast.LENGTH_LONG).show();
        else
        if(temp_password.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Password!",Toast.LENGTH_LONG).show();
        else
        if(temp_confirm.isEmpty()==true)
            Toast.makeText(this,"Please Confirm Your Password!",Toast.LENGTH_LONG).show();
        else
        if(temp_address.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Address!",Toast.LENGTH_LONG).show();
        else
        if(temp_contact.isEmpty()==true)
            Toast.makeText(this,"Please Enter Your Contact!",Toast.LENGTH_LONG).show();
        else if(temp_password!=temp_confirm)
            Toast.makeText(this,"The passwords do not match!",Toast.LENGTH_LONG).show();
        else
        {
            progressDialog.setMessage("Registering User...");
            progressDialog.show();

        }

    }

}
