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
    private static final String TAG="LoginActivity";
    EditText username;
    EditText password;
    Button   Login;
    private FirebaseAuth firebaseAuth;
    boolean flag;
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
        firebaseAuth =FirebaseAuth.getInstance();
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
        //method call to check if user exists, and if exists, then redirect it to profile.
        if(temp_username.isEmpty()==false&&temp_password.isEmpty()==false)
            checklogin(temp_username,temp_password);
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
            checklogin2(account);

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
            startActivity(new Intent(login.this, UserHome.class));
        }
        super.onStart();
    }


    // method to check if user exists.
    public void checklogin(final String temp_username, final String temp_password)
    {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final ProgressDialog progressDialog =new ProgressDialog(login.this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(temp_username, temp_password)
                .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if the task is successful
                        if(task.isSuccessful()){
                            //start the profile activity
                            DatabaseReference root = firebaseDatabase.getReference();
                            root.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    if(currentuser.isEmpty()){
                                        Toast.makeText(login.this,"Error",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        SharedPreferencesApp.setSessionState(login.this,"User");
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), UserHome.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            Toast.makeText(login.this,"Incorrect credentials",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }


    public void checklogin2(GoogleSignInAccount account)
    {

        final DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        final String temp_id = account.getId();
        final String personName=account.getDisplayName().trim();
        final String personEmail=account.getEmail().trim();
        final String personContact="".trim();
        final String personAddress="".trim();
        final String username=personEmail.trim();
        final User u=new User(personName,personContact,personAddress);
        root.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child(temp_id).exists())
                    {
                        root.child("Users").child(temp_id).setValue(u);
                    }
                    startActivity(new Intent(login.this, UserHome.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
