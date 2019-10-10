package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    String name,pass,contact,address,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Bundle bundle = getIntent().getExtras();
        sign_out = findViewById(R.id.log_out);
        nameTV = findViewById(R.id.name);
        emailTV = findViewById(R.id.email);
        contactTV=findViewById(R.id.contact);
        addressTV=findViewById(R.id.address);
        change_pwd=findViewById(R.id.ChangePwd);
        Edit =findViewById(R.id.EditProfile);
        if(bundle!=null) {
            if (!bundle.getString("email").isEmpty())
                email = bundle.getString("email");
        }
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Users").child(email).child("name").getValue().toString();
                address= decodeFirebase(dataSnapshot.child("Users").child(email).child("address").getValue().toString());
                contact=dataSnapshot.child("Users").child(email).child("contact").getValue().toString();
                nameTV.setText(name);
                emailTV.setText(decodeFirebase(email));
                contactTV.setText(contact);
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

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(UserProfile.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();

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
                Intent i= new Intent(UserProfile.this,ChangePassword.class);
                i.putExtra("username",email);
                startActivity(i);
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
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UserProfile.this,"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserProfile.this, CustomerMain.class));
                        finish();
                    }
                });
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
