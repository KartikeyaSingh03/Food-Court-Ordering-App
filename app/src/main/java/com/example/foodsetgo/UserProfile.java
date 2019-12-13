package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    Button sign_out;
    Button change_pwd;
    Button Edit;
    TextView nameTV;
    TextView emailTV;
    TextView contactTV;
    TextView addressTV;
    String Name="",pass,contact="",address="",email,currentuser;
    Boolean flag=true;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        sign_out = findViewById(R.id.log_out);
        nameTV = findViewById(R.id.name);
        emailTV = findViewById(R.id.email);
        contactTV=findViewById(R.id.contact);
        addressTV=findViewById(R.id.address);
        firebaseAuth = FirebaseAuth.getInstance();
        change_pwd=findViewById(R.id.ChangePwd);
        Edit =findViewById(R.id.EditProfile);

        GoogleSignInAccount acct =  GoogleSignIn.getLastSignedInAccount(UserProfile.this);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
        else {
            currentuser = acct.getId();
            email= acct.getEmail();
            flag = false;
        }

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(currentuser).child("name").exists())
                    Name = dataSnapshot.child("Users").child(currentuser).child("name").getValue().toString();
                if(dataSnapshot.child("Users").child(currentuser).child("address").exists())
                    address=dataSnapshot.child("Users").child(currentuser).child("address").getValue().toString();
                if(dataSnapshot.child("Users").child(currentuser).child("contact").exists())
                    contact=dataSnapshot.child("Users").child(currentuser).child("contact").getValue().toString();
                    if(!Name.isEmpty())
                        nameTV.setText(Name);
                    if(!email.isEmpty())
                        emailTV.setText(email);
                    if(!contact.isEmpty())
                        contactTV.setText(contact);
                    if(!address.isEmpty())
                        addressTV.setText(address);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile.this,"Not Retriving",Toast.LENGTH_LONG).show();
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            nameTV.setText(personName);
            emailTV.setText(personEmail);
        }

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    Intent i= new Intent(UserProfile.this,ChangePassword.class);
                    i.putExtra("username",email);
                    startActivity(i);
                }
                else{
                    Toast.makeText(UserProfile.this,"Google Users cannot change password",Toast.LENGTH_LONG).show();
                }
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(UserProfile.this,EditDetails.class);
                i.putExtra("Email",email);
                startActivity(i);
            }
        });
    }

    private void signOut() {
        if(flag){
            AlertDialog.Builder alert = new AlertDialog.Builder(UserProfile.this);
            alert.setMessage("Are you sure you want to logout?").setCancelable(false).setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent in = new Intent(UserProfile.this,CustomerMain.class);

                    startActivity(in);
                    Toast.makeText(UserProfile.this,"Logged out Successfully",Toast.LENGTH_LONG).show();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).show();
        }
        else {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(UserProfile.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserProfile.this, CustomerMain.class));
                            finish();
                        }
                    });
        }
    }

}
