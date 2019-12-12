package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;

public class SignUp1 extends AppCompatActivity {
    EditText name;
    EditText address;
    EditText contact;
    Button register;
    String temp_password,temp_username;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);
        name= findViewById(R.id.name);
        contact= findViewById(R.id.number);
        address= findViewById(R.id.address);
        register= findViewById(R.id.register);
        Bundle bundle = getIntent().getExtras();
        database = FirebaseDatabase.getInstance();
        temp_username=bundle.getString("username");
        final  String username=temp_username.trim();
        temp_password=bundle.getString("password");
        final String pass=temp_password.trim();
        firebaseAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register(username,pass);
            }
        });
    }

    public void Register(final String username, String password)
    {
        final String temp_name=name.getText().toString().trim();
        final String temp_address=address.getText().toString().trim();
        final String temp_contact=contact.getText().toString().trim();
        try {
            if (temp_name.isEmpty() == true || isValidName(temp_name) == false)
                Toast.makeText(this, "Please Enter Your Name!", Toast.LENGTH_LONG).show();
            else if (temp_address.isEmpty() == true || isValidAdd(temp_address) == false)
                Toast.makeText(this, "Please Enter Your Address!", Toast.LENGTH_LONG).show();
            else if (temp_contact.isEmpty() == true || isValidContact(temp_contact) == false)
                Toast.makeText(this, "Please Enter a Valid Contact Number!", Toast.LENGTH_LONG).show();
            else {
                final ProgressDialog progress = new ProgressDialog(SignUp1.this);
                progress.setMessage("Registering...");
                progress.show();
                firebaseAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(SignUp1.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if success
                                if(task.isSuccessful()){
                                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference root = database.getReference();
                                    final User u = new User(temp_name, temp_contact, temp_address);
                                    root.child("Users").child(currentuser).setValue(u);
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), UserHome.class));
                                }else{
                                    //display some message here
                                    Toast.makeText(SignUp1.this,"Email ID is already registered",Toast.LENGTH_LONG).show();
                                }
                                progress.dismiss();
                            }
                        });

            }
        }
        catch (Exception e){
            Toast.makeText(SignUp1.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public boolean isValidContact(String s){
        if(s.length()!=10)
            return false;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)<'0'||s.charAt(i)>'9')
                return false;
        }
        return true;
    }

    public boolean isValidName(String s){

        for(int i=0;i<s.length();i++){
            char c =s.charAt(i);
            if(Character.isLetter(c)==false&&c!=' ')
                return false;
        }
        return true;
    }

    public boolean isValidAdd(String s){
        for(int i=0;i<s.length();i++){
            char c =s.charAt(i);
            if(Character.isLetter(c)==false&&c!='-'&&c!='/'&&Character.isDigit(c)==false&&c!=' '&&c!='('&&c!=')'&&c!='.'&&c!=',')
                return false;
        }
        return true;
    }


}