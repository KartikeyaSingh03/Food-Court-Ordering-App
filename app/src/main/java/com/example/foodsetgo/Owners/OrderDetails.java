package com.example.foodsetgo.Owners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.foodsetgo.R;
import com.example.foodsetgo.User;
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
    Spinner spinner;
    Button update_status;
    String UserNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        final Bundle bundle = getIntent().getExtras();
        UserID= bundle.getString("UID");
        OrderNo= bundle.getString("UserOrderNo");
        Add= findViewById(R.id.Address);
        Cont =findViewById(R.id.Contact);
        Price=findViewById(R.id.Total);
        spinner=findViewById(R.id.status_spinner);
        update_status = findViewById(R.id.update_status);
        RestId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        database=FirebaseDatabase.getInstance();

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(OrderDetails.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Order_status));


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        final DatabaseReference root = database.getReference();
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
        root.child("Restaurants").child(RestId).child("orders").child(bundle.getString("RestOrderNo")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                price=dataSnapshot.child("GrandTotal").getValue().toString();
                status=dataSnapshot.child("Status").getValue().toString();
                Price.setText("Price: "+price);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String status = spinner.getSelectedItem().toString();

                root.child("Users").child(UserID).child("orders").child(OrderNo).child("Status").setValue(status);
                root.child("Users").child(UserID).child("orders").child(OrderNo).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String OrderNumRest = dataSnapshot.child("OrderNumRest").getValue(String.class);
                        root.child("Restaurants").child(RestId).child("orders").child(bundle.getString("RestOrderNo")).child("Status").setValue(status);
                        Intent intent = new Intent(OrderDetails.this,Res_Home.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
