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

public class UserProfile extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    Button sign_out;
    Button change_pwd;
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
        if(bundle!=null) {
            if (!bundle.getString("name").isEmpty())
                name = bundle.getString("name");
            if (!bundle.getString("pass").isEmpty())
                pass = bundle.getString("pass");
            if (!bundle.getString("contact").isEmpty())
                contact = bundle.getString("contact");
            if (!bundle.getString("address").isEmpty())
                address = bundle.getString("address");
            if (!bundle.getString("email").isEmpty())
                email = decodeFirebase(bundle.getString("email"));
        }
        nameTV.setText(name);
        emailTV.setText(email);
        contactTV.setText(contact);
        addressTV.setText(address);
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
                i.putExtra("password",pass);
                i.putExtra("contact",contact);
                i.putExtra("address",address);
                i.putExtra("name",name);
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
