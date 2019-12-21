package com.example.foodsetgo.Owners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodsetgo.EditDetails;
import com.example.foodsetgo.R;
import com.example.foodsetgo.User;
import com.example.foodsetgo.UserHome;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDetailsOwner extends AppCompatActivity {
    EditText NameTF,ContTF,AddTF;
    Button Modify;
    String username,Uid,photoURL;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details_owner);
        NameTF=findViewById(R.id.ChangeName);
        ContTF=findViewById(R.id.ChangeContact);
        AddTF=findViewById(R.id.ChangeAddress);
        Modify=findViewById(R.id.ChangeButton);
        Bundle bundle =getIntent().getExtras();
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        Uid = firebaseAuth.getCurrentUser().getUid();
        Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newName = NameTF.getText().toString().trim();
                final String newContact = ContTF.getText().toString().trim();
                final String newAdd = AddTF.getText().toString().trim();
                if (newName.isEmpty() == true || isValidName(newName) == false)
                    Toast.makeText(EditDetailsOwner.this, "Please Enter Your Name!", Toast.LENGTH_LONG).show();
                else if (newAdd.isEmpty() == true || isValidAdd(newAdd) == false)
                    Toast.makeText(EditDetailsOwner.this, "Please Enter Your Address!", Toast.LENGTH_LONG).show();
                else if (newContact.isEmpty() == true || isValidContact(newContact) == false)
                    Toast.makeText(EditDetailsOwner.this, "Please Enter a Valid Contact Number!", Toast.LENGTH_LONG).show();
                else {
                    try {
                        final ProgressDialog progress = new ProgressDialog(EditDetailsOwner.this);
                        progress.setMessage("Editing Details...");
                        progress.show();
                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("Restaurants").child(Uid).exists()){
                                    root.child("Restaurants").child(Uid).child("name").setValue(newName);
                                    root.child("Restaurants").child(Uid).child("contact").setValue(newContact);
                                    root.child("Restaurants").child(Uid).child("address").setValue(newAdd);
                                    Toast.makeText(EditDetailsOwner.this, "Details Edited Successfully!", Toast.LENGTH_LONG).show();
                                    progress.dismiss();
                                    Intent i = new Intent(EditDetailsOwner.this, Res_Home.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(EditDetailsOwner.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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
