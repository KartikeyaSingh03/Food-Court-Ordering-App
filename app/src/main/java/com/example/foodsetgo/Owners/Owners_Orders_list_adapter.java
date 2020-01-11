package com.example.foodsetgo.Owners;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.R;
import com.example.foodsetgo.User;
import com.example.foodsetgo.fooditem;
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

public class Owners_Orders_list_adapter extends  RecyclerView.Adapter<Owners_Orders_list_adapter.ViewHolder> {

    private List<Pair<String,Pair<String,String>>> listUser;
    private Context context;

    public Owners_Orders_list_adapter(List<Pair<String,Pair<String,String>>> listmenus, Context context) {
        this.listUser = listmenus;
        this.context = context;
    }


    @NonNull
    @Override
    public Owners_Orders_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.owners_user_cards,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Owners_Orders_list_adapter.ViewHolder holder, int position) {
        final Pair<String,Pair<String,String>> User = listUser.get(position);
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();

        root = root.child("Users").child(User.first);
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child("name").getValue(String.class);
                holder.UserName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef=storage.getReferenceFromUrl("gs://foodsetgo-120b6.appspot.com");
        storageRef.child("uploads/"+User.first).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                Picasso.get().load(R.drawable.main_image).into(holder.imageView);

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),OrderDetails.class);
                intent.putExtra("UID",User.first);
                intent.putExtra("UserOrderNo",User.second.first);
                intent.putExtra("RestOrderNo",User.second.second);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView UserName;
        public ImageView imageView;
        public CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName=itemView.findViewById(R.id.userName);
            imageView=itemView.findViewById(R.id.userImage);
            cardView=itemView.findViewById(R.id.owners_user_cards);
        }
    }


}