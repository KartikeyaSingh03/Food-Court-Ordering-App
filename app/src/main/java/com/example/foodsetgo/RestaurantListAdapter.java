package com.example.foodsetgo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> implements Filterable {

    private List<RestInfo> listRests;
    private List<RestInfo> ListRestsCopy;
    private Context context;
    String uid;
    public RestaurantListAdapter(List<RestInfo> listRests, Context context) {
        this.listRests = listRests;
        this.context = context;
        ListRestsCopy= new ArrayList<>(listRests);
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
        final RestInfo listRest=listRests.get(position);
        holder.RestName.setText(listRest.getName());
        FirebaseStorage storage=FirebaseStorage.getInstance();

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
            uid=firebaseAuth.getCurrentUser().getUid();
        StorageReference storageRef=storage.getReferenceFromUrl("gs://foodsetgo-120b6.appspot.com");


        storageRef.child("images/"+listRest.getRestUid()+'/'+"Restaurant_Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                Intent i=new Intent(context,restaurantMenu.class);
                i.putExtra("UID",listRest.getRestUid());
                context.startActivity(i);
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
        public CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            RestName =itemView.findViewById(R.id.RestName);
            imageView=itemView.findViewById(R.id.RestImage);
            card=itemView.findViewById(R.id.Rest_cards);
        }
    }

    public Filter getFilter(){
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<RestInfo> filteredList = new ArrayList<>();
            if(charSequence==null||charSequence.length()==0){
                filteredList.addAll(ListRestsCopy);
            }
            else{
                String filterPattern= charSequence.toString().toLowerCase().trim();

                for(RestInfo info : ListRestsCopy){
                    if(info.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(info);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listRests.clear();
            listRests.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
