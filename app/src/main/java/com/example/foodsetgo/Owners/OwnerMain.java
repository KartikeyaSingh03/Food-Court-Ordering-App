package com.example.foodsetgo.Owners;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsetgo.R;
import com.example.foodsetgo.SharedPreferencesApp;
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

public class OwnerMain extends AppCompatActivity {

    EditText emailaddress;
    EditText password;
    Button   login;
    private FirebaseAuth firebaseAuth;

    Button   signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
        emailaddress=findViewById(R.id.edit_email);
        password=findViewById(R.id.edit_password);
        login=findViewById(R.id.login);
        firebaseAuth =FirebaseAuth.getInstance();

        signup=findViewById(R.id.signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_username=(emailaddress.getText().toString().trim());
                String temp_password=(password.getText().toString().trim());
                if(temp_password.isEmpty()||temp_username.isEmpty())
                    Toast.makeText(OwnerMain.this,"Please Give Required Informations",Toast.LENGTH_LONG).show();
                else
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

    // method to check if user exists.
    public void checklogin(final String temp_username, final String temp_password)
    {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final ProgressDialog progressDialog =new ProgressDialog(OwnerMain.this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(temp_username, temp_password)
                .addOnCompleteListener(OwnerMain.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if the task is successful
                        if(task.isSuccessful()){
                            //start the profile activity
                            DatabaseReference root = firebaseDatabase.getReference();
                            root.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    if(currentuser.isEmpty()){
                                        Toast.makeText(OwnerMain.this,"Error",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Intent i=new Intent(OwnerMain.this,Res_Home.class);
                                        SharedPreferencesApp.setSessionState(OwnerMain.this,"Owner");
                                        finish();
                                        startActivity(i);
                                   }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            Toast.makeText(OwnerMain.this,"Incorrect credentials",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

}
