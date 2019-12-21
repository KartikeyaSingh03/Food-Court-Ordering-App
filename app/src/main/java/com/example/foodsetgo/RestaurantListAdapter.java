package com.example.foodsetgo;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.Owners.Res_Home;
import com.example.foodsetgo.Owners.RestInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private List<RestInfo> listRests;
    private Context context;
    String uid;
    public RestaurantListAdapter(List<RestInfo> listRests, Context context) {
        this.listRests = listRests;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final RestInfo listmenu=listRests.get(position);
        holder.RestName.setText(listmenu.getName());
        FirebaseStorage storage=FirebaseStorage.getInstance();

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
            uid=firebaseAuth.getCurrentUser().getUid();
        StorageReference storageRef=storage.getReferenceFromUrl("gs://foodsetgo-120b6.appspot.com");


        storageRef.child("images/"+listmenu.getRestUid()+'/'+"Restaurant_Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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


    }

    @Override
    public int getItemCount() {
        return listRests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView RestName;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            RestName =itemView.findViewById(R.id.RestName);
            imageView=itemView.findViewById(R.id.RestImage);
        }
    }
}
