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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditDetails extends AppCompatActivity {
    EditText NameTF,EmailTF,ContTF,AddTF;
    Button Modify;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        NameTF=findViewById(R.id.ChangeName);
        EmailTF=findViewById(R.id.ChangeEmail);
        ContTF=findViewById(R.id.ChangeContact);
        AddTF=findViewById(R.id.ChangeAddress);
        Modify=findViewById(R.id.ChangeButton);
        Bundle bundle =getIntent().getExtras();
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        if(bundle!=null){
            username= encodeFirebase(bundle.getString("Email"));
        }
        Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newName = NameTF.getText().toString().trim();
                final String newEmail = EmailTF.getText().toString().trim();
                final String newContact = ContTF.getText().toString().trim();
                final String newAdd = AddTF.getText().toString().trim();
                if (newEmail.isEmpty() == true)
                    Toast.makeText(EditDetails.this, "Please Enter Your Email!", Toast.LENGTH_LONG).show();
                else if (isValidEmail(newEmail) == false || EmailChar(newEmail) == false)
                    Toast.makeText(EditDetails.this, "Email ID is invalid", Toast.LENGTH_LONG).show();
                else if (newName.isEmpty() == true || isValidName(newName) == false)
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
                        root.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("Users").child(encodeFirebase(newEmail)).exists()){
                                    Toast.makeText(EditDetails.this,"The username already exists",Toast.LENGTH_LONG).show();
                                    progress.dismiss();
                                    Intent i = new Intent(EditDetails.this,UserProfile.class);
                                    i.putExtra("email",username);
                                }
                                else {
                                    String newPass = dataSnapshot.child("Users").child(username).child("password").getValue().toString();
                                    User newUser = new User(newName, newPass, newContact, encodeFirebase(newAdd));
                                    root.child("Users").child(username).removeValue();
                                    root.child("Users").child(encodeFirebase(newEmail)).setValue(newUser);
                                    Toast.makeText(EditDetails.this, "Details Edited Successfully!", Toast.LENGTH_LONG).show();
                                    progress.dismiss();
                                    Intent i = new Intent(EditDetails.this, UserProfile.class);
                                    i.putExtra("email", encodeFirebase(newEmail));
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

    public static String encodeFirebase(String s) {
        return s
                .replace("-", "+")
                .replace(".", ">")
                .replace("/", "?")
                .replace("_","=");
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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean EmailChar(String s){
        for(int i=0;i<s.length();i++){
            char c =s.charAt(i);
            if(Character.isLetter(c)==false&&Character.isDigit(c)==false&&c!='.'&&c!='_'&&c!='@')
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
