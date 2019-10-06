package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;

public class OwnerMain extends AppCompatActivity {
    EditText emailaddress;
    EditText password;
    Button   login;
    Button   signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        emailaddress=findViewById(R.id.edit_email);
        password=findViewById(R.id.edit_password);
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_username=sha256(emailaddress.getText().toString().trim());
                String temp_password=sha256(password.getText().toString().trim());
                checklogin(temp_username,temp_password);

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(OwnerMain.this,RegistrationRestaurant.class);
                startActivity(i);
            }
        });
    }

    public void checklogin(final String temp_username, final String temp_password)
    {
        DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        root.child("Restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()==true)
                {
                    if(dataSnapshot.child(temp_username).exists()==true)
                    {
                        if(dataSnapshot.child(temp_username).child("password").getValue().toString().equals(temp_password))
                        {
                            Intent i=new Intent(OwnerMain.this,UserProfile.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(OwnerMain.this,"Invalid Password",Toast.LENGTH_LONG).show();


                        }
                    }
                    else
                    {
                        Toast.makeText(OwnerMain.this,"Invalid Username",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}
