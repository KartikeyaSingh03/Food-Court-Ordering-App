package com.example.foodsetgo.Owners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.foodsetgo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetails extends AppCompatActivity {

    TextView Add,Cont,Price,Status;
    String UserID,OrderNo;
    String address,contact,price,status,RestId;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Bundle bundle = getIntent().getExtras();
        UserID= bundle.getString("UID");
        OrderNo= bundle.getString("OrderNo");
        Add= findViewById(R.id.Address);
        Cont =findViewById(R.id.Contact);
        Price=findViewById(R.id.Total);
        Status=findViewById(R.id.Status);
        RestId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        database=FirebaseDatabase.getInstance();
        DatabaseReference root = database.getReference();
        root.child("Users").child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contact= dataSnapshot.child("contact").getValue().toString();
                address= dataSnapshot.child("address").getValue().toString();
                Cont.setText("Contact: "+contact);
                Add.setText("Address: "+address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        root.child("Restaurants").child(RestId).child("orders").child(OrderNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                price=dataSnapshot.child("GrandTotal").getValue().toString();
                status=dataSnapshot.child("Status").getValue().toString();
                Price.setText("Price: "+price);
                Status.setText("Status: "+status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
