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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;

public class SignUp1 extends AppCompatActivity {
    EditText name;
    EditText address;
    EditText contact;
    Button register;
    String temp_password,temp_username;
    FirebaseDatabase database;
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
        final  String username=encodeFirebase(temp_username).trim();
        temp_password=bundle.getString("password");
        final String pass=sha256(temp_password).trim();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Register(username,pass);
            }
        });
    }

    public void Register(String username,String password)
    {
        String temp_name=name.getText().toString().trim();
        String temp_address=address.getText().toString().trim();
        String temp_contact=contact.getText().toString().trim();
        try {
            if (temp_name.isEmpty() == true || isValidName(temp_name) == false)
                Toast.makeText(this, "Please Enter Your Name!", Toast.LENGTH_LONG).show();
            else if (temp_address.isEmpty() == true || isValidAdd(temp_address) == false)
                Toast.makeText(this, "Please Enter Your Address!", Toast.LENGTH_LONG).show();
            else if (temp_contact.isEmpty() == true || isValidContact(temp_contact) == false)
                Toast.makeText(this, "Please Enter a Valid Contact Number!", Toast.LENGTH_LONG).show();
            else {

                temp_address = encodeFirebase(temp_address);
                //database.setPersistenceEnabled(true);
                final ProgressDialog progress = new ProgressDialog(SignUp1.this);
                progress.setMessage("Registering...");
                progress.show();
                User u = new User(temp_name, password, temp_contact, temp_address);
                DatabaseReference root = database.getReference();
                root.child("Users").child(username).setValue(u)
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful() == true) {
                                    Toast.makeText(SignUp1.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                    progress.dismiss();
                                } else {
                                    Toast.makeText(SignUp1.this, "Registration UnSuccessful", Toast.LENGTH_LONG).show();
                                    progress.dismiss();

                                }
                            }
                        });
                name.setText("");
                address.setText("");
                contact.setText("");
                Intent i = new Intent(SignUp1.this, UserProfile.class);
                i.putExtra("email",username);
                startActivity(i);

            }
        }
        catch (Exception e){
            Toast.makeText(SignUp1.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
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

    public static String encodeFirebase(String s) {
        return s
                .replace("-", "+")
                .replace(".", ">")
                .replace("/", "?")
                .replace("_","=");
    }

    public static String decodeFirebase(String s) {
        String res="";
        for(int ni=0;ni<s.length();ni++) {
            char nc = s.charAt(ni);
            if (nc == '+') {
                res += '-';
            }
            else if (nc == '>') {
                res += '.';
            }
            else if (nc == '?') {
                res += '/';
            }
            else if(nc == '='){
                res+='_';
            }
            else {
                res+=s.charAt(ni);
            }
        }
        return res;
    }
}