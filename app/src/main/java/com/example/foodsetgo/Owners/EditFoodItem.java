package com.example.foodsetgo.Owners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodsetgo.R;
import com.example.foodsetgo.fooditem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditFoodItem extends AppCompatActivity {
    Spinner spinner;
    EditText foodname,price;
    Button add,btnchoose;
    Uri filePath;
    String imgname,FoodName;
    private ImageView imageView;
    Boolean flag=false;
    String email,currentuser,Oldname;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FloatingActionButton change_btn;
    public final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_item);
        spinner=findViewById(R.id.spinner);
        foodname=findViewById(R.id.foodname);
        price=findViewById(R.id.price);
        add=findViewById(R.id.additem);
        btnchoose=findViewById(R.id.chooseimage);
        change_btn=findViewById(R.id.fab);
        imageView = (ImageView) findViewById(R.id.imgView);
        add.setEnabled(false);
        btnchoose.setEnabled(false);
        price.setEnabled(false);
        foodname.setEnabled(false);
        price.setTextColor(Color.parseColor("#D3D3D3"));
        foodname.setTextColor(Color.parseColor("#D3D3D3"));
        spinner.setEnabled(false);
        firebaseAuth=FirebaseAuth.getInstance();
        currentuser = firebaseAuth.getCurrentUser().getUid();
        database=FirebaseDatabase.getInstance();
        email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
            FoodName=bundle.getString("FoodName");
        final DatabaseReference root = database.getReference().child("Restaurants").child(currentuser).child("menu");
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(EditFoodItem.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.status));


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Oldname= dataSnapshot.child(FoodName).child("Name").getValue().toString();
                String Price= dataSnapshot.child(FoodName).child("Price").getValue().toString();
                String Type= dataSnapshot.child(FoodName).child("Status").getValue().toString();
                foodname.setText(Oldname);
                price.setText(Price);
                if(Type.equals("VEG"))
                    spinner.setSelection(0);
                else
                    spinner.setSelection(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        StorageReference ref = storageRef.child("images").child(currentuser).child(FoodName);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Image");
        progressDialog.show();
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                filePath=uri;
                Glide.with(EditFoodItem.this).asBitmap().load(uri).into(imageView);
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditFoodItem.this,"Failed to Load Image",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });


        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.setEnabled(true);
                btnchoose.setEnabled(true);
                price.setEnabled(true);
                spinner.setEnabled(true);
                price.setTextColor(Color.parseColor("#000000"));
            }
        });

        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String text=spinner.getSelectedItem().toString().trim();

                final String itemname=Oldname;
                final String itemprice=price.getText().toString().trim();

                final fooditem temp = new fooditem(itemname,itemprice, text,"",email);

                DatabaseReference root= FirebaseDatabase.getInstance().getReference();
                root.child("Restaurants").child(currentuser).child("menu").child(temp.getName()).child("Price").setValue(temp.getPrice());
                root.child("Restaurants").child(currentuser).child("menu").child(temp.getName()).child("Status").setValue(temp.getStatus());
                root.child("Restaurants").child(currentuser).child("menu").child(temp.getName()).child("Username").setValue(temp.getUsername());
                if(flag)
                    uploadImage(temp.getName(),currentuser);
                else{
                    Toast.makeText(EditFoodItem.this, "Changed Details Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),Res_Home.class));
                }

            }



        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            flag=true;
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageRef=storage.getReference();
    private void uploadImage(String Name,String RestUid) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageRef.child("images").child(RestUid).child(Name);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(EditFoodItem.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),Res_Home.class));


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditFoodItem.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

}
