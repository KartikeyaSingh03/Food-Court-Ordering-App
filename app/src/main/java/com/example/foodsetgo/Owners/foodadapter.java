package com.example.foodsetgo.Owners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.R;
import com.example.foodsetgo.fooditem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class foodadapter extends RecyclerView.Adapter<foodadapter.ViewHolder> {

    private List<fooditem> listmenus;
    private Context context;

    public foodadapter(List<fooditem> listmenus, Context context) {
        this.listmenus = listmenus;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_food,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final fooditem listmenu=listmenus.get(position);
        holder.foodname.setText(listmenu.getName());
        FirebaseStorage storage=FirebaseStorage.getInstance();

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        String uid=firebaseAuth.getCurrentUser().getUid();
        StorageReference storageRef=storage.getReferenceFromUrl("gs://foodsetgo-120b6.appspot.com");
        storageRef.child("images/"+uid+'/'+listmenu.getName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                holder.foodname.setText("FAIL");

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),EditFoodItem.class);
                i.putExtra("FoodName",listmenu.getName());
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listmenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView foodname;
        public ImageView imageView;
        public CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodname=itemView.findViewById(R.id.foodname);
            imageView=itemView.findViewById(R.id.foodimage);
            cardView=itemView.findViewById(R.id.courseItem);
        }
    }


}

