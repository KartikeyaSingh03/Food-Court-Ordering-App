package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class ChangePassword extends AppCompatActivity {
    private Button change;
    private EditText confPwd;
    private EditText newPass;
    private EditText oldPass;
    FirebaseDatabase database;
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
        database = FirebaseDatabase.getInstance();
        final DatabaseReference root = database.getReference();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldPassword=oldPass.getText().toString().trim();
                final String newPassword=newPass.getText().toString().trim();
                final String confPassword=confPwd.getText().toString().trim();
                root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pwd = dataSnapshot.child("Users").child(username).child("password").getValue().toString();
                        contact = dataSnapshot.child("Users").child(username).child("contact").getValue().toString();
                        name = dataSnapshot.child("Users").child(username).child("name").getValue().toString();
                        address = dataSnapshot.child("Users").child(username).child("address").getValue().toString();
                        if(sha256(oldPassword).equals(pwd)){
                            if(newPassword.equals(confPassword)){
                                if(passStrength(newPassword)){
                                    final ProgressDialog progress = new ProgressDialog(ChangePassword.this);
                                    progress.setMessage("Changing Password ");
                                    progress.show();
                                    User u= new User(name,contact,address);
                                    root.child("Users").child(username).setValue(u);
                                    progress.dismiss();
                                    Toast.makeText(ChangePassword.this,"Password Changed Successfully",Toast.LENGTH_LONG).show();
                                    Intent i= new Intent(ChangePassword.this,UserProfile.class);
                                    i.putExtra("email",username);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(ChangePassword.this, "Password must be at least 8 characters long, must contain a letter[a-z,A-Z], and a number[0-9]", Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(ChangePassword.this,"The Passwords do not match",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(ChangePassword.this, "Old password is incorrect", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
