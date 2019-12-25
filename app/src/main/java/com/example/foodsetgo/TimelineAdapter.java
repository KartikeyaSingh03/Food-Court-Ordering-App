package com.example.foodsetgo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private List<TimelineRestStruct> TimelineRests;
    private Context context;
    public TimelineAdapter(List<TimelineRestStruct> listRests, Context context) {
        this.TimelineRests = listRests;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_rest_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TimelineRestStruct Restinfo=TimelineRests.get(position);
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Restinfo.getUid()).exists())
                {
                    dataSnapshot = dataSnapshot.child(Restinfo.getUid());
                    holder.RestName.setText(dataSnapshot.child("name").getValue(String.class));
                    holder.grand_total.setText(Restinfo.getGrandTotal());
                    holder.status.setText(Restinfo.status);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef=storage.getReferenceFromUrl("gs://foodsetgo-120b6.appspot.com").child("images");
        storageRef.child(Restinfo.getUid()+'/'+"Restaurant_Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent i = new Intent(context,OrderList.class);
            i.putExtra("UID",Restinfo.getUid());
            i.putExtra("OrderNumber",Restinfo.getOrderNumber());

            context.startActivity(i);

            }

        });

    }

    @Override
    public int getItemCount() {
        return TimelineRests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView RestName;
        public ImageView imageView;
        public CardView card;
        public TextView status;
        public TextView grand_total;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            RestName =itemView.findViewById(R.id.timeline_rest_name);
            imageView=itemView.findViewById(R.id.timeline_rest_image);
            card=itemView.findViewById(R.id.timeline_rest_card);
            status = itemView.findViewById(R.id.timeline_rest_status);
            grand_total = itemView.findViewById(R.id.timeline_rest_grand_total);

        }
    }


}
