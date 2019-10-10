package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;

import java.security.MessageDigest;

public class login extends AppCompatActivity {
    int RC_SIGN_IN=0;
    EditText username;
    EditText password;
    Button   Login;
    int flag;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText) findViewById(R.id.edit_email);
        password= (EditText) findViewById(R.id.edit_password);
        Login= (Button) findViewById(R.id.login);
        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        String user_name = username.getText().toString();
        String pwd=password.getText().toString();
        signInButton = findViewById(R.id.sign_in_button);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manualLogin();
            }
        });


    }

    private void manualLogin(){
        final String temp_username=username.getText().toString().trim();
        final String temp_password=password.getText().toString().trim();
        final String pass=sha256(temp_password).trim();
        final String hashedusername=encodeFirebase(temp_username);
        //method call to check if user exists, and if exists, then redirect it to profile.
        if(temp_username.isEmpty()==false&&temp_password.isEmpty()==false)
            checklogin(hashedusername,pass);
        else
            Toast.makeText(login.this,"Enter a Username/Password",Toast.LENGTH_LONG).show();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //pushing user data to firebase.
            final String personName=account.getDisplayName().trim();
            final String personEmail=account.getEmail().trim();
            final String personPassword="".trim();
            final String personContact="".trim();
            final String personAddress="".trim();
            final String username=encodeFirebase(personEmail).trim();

            User u=new User(personName,personPassword,personContact,personAddress);

            DatabaseReference root=FirebaseDatabase.getInstance().getReference();
            checklogin2(username);
            if(flag==0)
            root.child("GoogleUsers").child(username).setValue(u);


            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(login.this, UserProfile.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(login.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(login.this, UserProfile.class));
        }
        super.onStart();
    }


    // method to check if user exists.
    public void checklogin(final String temp_username, final String temp_password)
    {
        DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        root.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()==true)
                {
                    if(dataSnapshot.child(temp_username).exists()==true)
                    {
                        if(dataSnapshot.child(temp_username).child("password").getValue().toString().equals(temp_password))
                        {
                            Intent i=new Intent(login.this,UserProfile.class);
                            i.putExtra("email",temp_username);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(login.this,"Invalid Password",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(login.this,"Username Doesn't Exist",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    // hash method for passwords
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


    public static String encodeFirebase(String s) {
        return s
                .replace("-", "+")
                .replace(".", ">")
                .replace("/", "?")
                .replace("_","=");
    }


    public void checklogin2(final String temp_username)
    {
        DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        root.child("GoogleUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()==true)
                {
                    if(dataSnapshot.child(temp_username).exists()==true)
                    {
                        flag=1;
                    }
                    else
                    {
                        flag=0;
                    }
                }
                else
                {
                    flag=0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
