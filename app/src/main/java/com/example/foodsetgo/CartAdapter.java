package com.example.foodsetgo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.Owners.Res_Home;
import com.example.foodsetgo.Owners.RestInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Pair<String,String>> listfoods;
    private Context context;
    private String uid;
    public CartAdapter(List<Pair<String,String>> listfoods, Context context,String uid) {
        this.listfoods = listfoods;
        this.context = context;
        this.uid = uid;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_food_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Pair<String,String> foodpair = listfoods.get(position);

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root = root.child("Restaurants");

        holder.FoodName.setText(foodpair.first);
        holder.count.setText(foodpair.second);


        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    dataSnapshot = dataSnapshot.child(uid).child("menu");
                    if(dataSnapshot.exists())
                    {
                        if(dataSnapshot.child(foodpair.first).exists())
                        {
                            final fooditem food;
                            food = dataSnapshot.child(foodpair.first).getValue(fooditem.class);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageRef=storage.getReferenceFromUrl("gs://foodsetgo-120b6.appspot.com");
        storageRef.child("images/"+uid+'/'+foodpair.first).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                final String strurl=uri.toString();
                Picasso.get().load(strurl).into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                holder.FoodName.setText("FAIL");

            }
        });



    }

    @Override
    public int getItemCount() {
        return listfoods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView FoodName;
        public ImageView imageView;
        public CardView card;
        public TextView count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            FoodName =itemView.findViewById(R.id.cart_food_name);
            imageView=itemView.findViewById(R.id.cartImage);
            card=itemView.findViewById(R.id.cart_cards);
            count = itemView.findViewById(R.id.count_food);
        }
    }
}
