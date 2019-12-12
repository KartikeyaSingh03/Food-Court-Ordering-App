package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDetails extends AppCompatActivity {
    EditText NameTF,ContTF,AddTF;
    Button Modify;
    String username,Uid;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        NameTF=findViewById(R.id.ChangeName);
        ContTF=findViewById(R.id.ChangeContact);
        AddTF=findViewById(R.id.ChangeAddress);
        Modify=findViewById(R.id.ChangeButton);
        Bundle bundle =getIntent().getExtras();
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if(bundle!=null){
            username= bundle.getString("Email");
        }
        Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newName = NameTF.getText().toString().trim();
                final String newContact = ContTF.getText().toString().trim();
                final String newAdd = AddTF.getText().toString().trim();
                if(firebaseAuth.getCurrentUser()!=null)
                    Uid = firebaseAuth.getCurrentUser().getUid();
                else
                    Uid= GoogleSignIn.getLastSignedInAccount(EditDetails.this).getId();
                if (newName.isEmpty() == true || isValidName(newName) == false)
                    Toast.makeText(EditDetails.this, "Please Enter Your Name!", Toast.LENGTH_LONG).show();
                else if (newAdd.isEmpty() == true || isValidAdd(newAdd) == false)
                    Toast.makeText(EditDetails.this, "Please Enter Your Address!", Toast.LENGTH_LONG).show();
                else if (newContact.isEmpty() == true || isValidContact(newContact) == false)
                    Toast.makeText(EditDetails.this, "Please Enter a Valid Contact Number!", Toast.LENGTH_LONG).show();
                else {
                    try {
                        final ProgressDialog progress = new ProgressDialog(EditDetails.this);
                        progress.setMessage("Editing Details...");
                        progress.show();
                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("Users").child(Uid).exists()){
                                    User newUser = new User(newName, newContact, newAdd);
                                    root.child("Users").child(Uid).removeValue();
                                    root.child("Users").child(Uid).setValue(newUser);
                                    Toast.makeText(EditDetails.this, "Details Edited Successfully!", Toast.LENGTH_LONG).show();
                                    progress.dismiss();
                                    Intent i = new Intent(EditDetails.this, UserHome.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(EditDetails.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
