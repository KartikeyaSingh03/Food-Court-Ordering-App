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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;

public class ChangePassword extends AppCompatActivity {
    private Button change;
    private EditText confPwd;
    private EditText newPass;
    private EditText oldPass;
    FirebaseDatabase database;
    FirebaseUser user;
    String username,pwd,name,contact,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPass = findViewById(R.id.oldPwd);
        newPass = findViewById(R.id.newPwd);
        confPwd = findViewById(R.id.conf);
        change= findViewById(R.id.ChangeBtn);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            username = bundle.getString("username");
        }
        user =FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        final DatabaseReference root = database.getReference();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldPassword=oldPass.getText().toString().trim();
                final String newPassword=newPass.getText().toString().trim();
                final String confPassword=confPwd.getText().toString().trim();
                AuthCredential credential = EmailAuthProvider.getCredential(username,oldPassword);
                    if(newPassword.equals(confPassword)) {
                        if(passStrength(newPassword)) {
                            user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ChangePassword.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();
                                                        Intent i = new Intent(ChangePassword.this,UserHome.class);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(ChangePassword.this, "Unable to change password", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(ChangePassword.this, "Old password is incorrect", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        }
                        else{
                            Toast.makeText(ChangePassword.this, "Password must be at least 8 characters long, must contain a letter[a-z,A-Z], and a number[0-9]", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(ChangePassword.this,"The Passwords do not match",Toast.LENGTH_LONG).show();
                    }

                }
        });

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
}
