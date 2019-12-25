package com.example.foodsetgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodsetgo.Owners.EditFoodItem;
import com.example.foodsetgo.Owners.RestInfo;
import com.example.foodsetgo.Owners.foodadapter;
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
import java.util.HashMap;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {


    private List<fooditem> listmenus;
    private List<fooditem> listmenusCopy;
    private Context context;
    private String uid;
    public MenuAdapter(List<fooditem> listmenus, Context context,String uid) {
        this.listmenus = listmenus;
        this.context = context;
        this.uid=uid;
        listmenusCopy = new ArrayList<>(listmenus);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_menu,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final fooditem listmenu=listmenus.get(position);
        holder.foodname.setText(listmenu.getName());
        FirebaseStorage storage=FirebaseStorage.getInstance();

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
        holder.numberPicker.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view,final int oldVal,final int newVal) {
                final List<Pair<fooditem,String>> cart = ((GlobalCart)context.getApplicationContext()).getCART();


                if(newVal==0)
                {
                    for(int i = 0;i<cart.size();i++)
                    {
                        if(cart.get(i).first.getName()==listmenu.getName())
                        {
                            cart.remove(cart.get(i));
                            break;
                        }
                    }
                    ((GlobalCart)context.getApplicationContext()).setCART(cart);
                }
                else
                {
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                    root = root.child("Restaurants").child(uid).child("menu");

                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot = dataSnapshot.child(listmenu.getName());
                            final fooditem temp = dataSnapshot.getValue(fooditem.class);
                            int j=-1;
                            for(int i = 0;i<cart.size();i++)
                            {
                                if(cart.get(i).first.getName()==listmenu.getName())
                                {
                                    j=i;
                                    break;
                                }
                            }
                            if(j==-1)
                            {
                                Pair<fooditem,String> food = Pair.create(temp,Integer.toString(newVal));
                                cart.add(food);

                            }
                            else
                            {
                                Pair<fooditem,String> food = Pair.create(temp,Integer.toString(newVal));
                                cart.remove(cart.get(j));
                                cart.add(food);
                            }
                            ((GlobalCart)context.getApplicationContext()).setCART(cart);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                }
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
        public ElegantNumberButton numberPicker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodname=itemView.findViewById(R.id.FoodName_menu);
            imageView=itemView.findViewById(R.id.FoodImage_menu);
            cardView=itemView.findViewById(R.id.Menu_cards);
            numberPicker = itemView.findViewById(R.id.number_picker);
        }
    }

    public Filter getFilter(){
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<fooditem> filteredList = new ArrayList<>();
            if(charSequence==null||charSequence.length()==0){
                filteredList.addAll(listmenusCopy);
            }
            else{
                String filterPattern= charSequence.toString().toLowerCase().trim();
                for(fooditem item : listmenusCopy){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listmenus.clear();
            listmenus.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };


}
