package com.example.foodsetgo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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

public class ProfileFragment extends Fragment {
    GoogleSignInClient mGoogleSignInClient;
    Button sign_out;
    Button change_pwd;
    Button Edit;
    TextView nameTV;
    TextView emailTV;
    TextView contactTV;
    TextView addressTV;
    View Viewroot;
    String Name="",pass,contact="",address="",email,currentuser;
    Boolean flag=true;
    FirebaseAuth firebaseAuth;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        Viewroot=inflater.inflate(R.layout.fragment_profile, container, false);
        sign_out = Viewroot.findViewById(R.id.log_out);
        nameTV = Viewroot.findViewById(R.id.name);
        emailTV = Viewroot.findViewById(R.id.email);
        contactTV=Viewroot.findViewById(R.id.contact);
        addressTV=Viewroot.findViewById(R.id.address);
        firebaseAuth = FirebaseAuth.getInstance();
        change_pwd=Viewroot.findViewById(R.id.ChangePwd);
        Edit =Viewroot.findViewById(R.id.EditProfile);
        GoogleSignInAccount acct =  GoogleSignIn.getLastSignedInAccount(getContext());
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
                Toast.makeText(getContext(),"Not Retriving",Toast.LENGTH_LONG).show();
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

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
                    Intent i= new Intent(getContext(),ChangePassword.class);
                    i.putExtra("username",email);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getContext(),"Google Users cannot change password",Toast.LENGTH_LONG).show();
                }
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getContext(),EditDetails.class);
                i.putExtra("Email",email);
                startActivity(i);
            }
        });
        return Viewroot;
    }

    private void signOut() {
        if(flag){
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setMessage("Are you sure you want to logout?").setCancelable(false).setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferencesApp.setSessionState(getContext(),"NULL");
                    Intent in = new Intent(getContext(),CustomerMain.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    getActivity().finish();
                    Toast.makeText(getContext(),"Logged out Successfully",Toast.LENGTH_LONG).show();
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
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            SharedPreferencesApp.setSessionState(getContext(),"NULL");
                            Toast.makeText(getContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(getContext(),CustomerMain.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            getActivity().finish();
                        }
                    });
        }
    }

}
