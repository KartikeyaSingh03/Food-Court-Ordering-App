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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsetgo.Owners.EditFoodItem;
import com.example.foodsetgo.Owners.foodadapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {


    private List<fooditem> listmenus;
    private Context context;
    private String uid;
    public MenuAdapter(List<fooditem> listmenus, Context context,String uid) {
        this.listmenus = listmenus;
        this.context = context;
        this.uid=uid;
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
        holder.numberPicker.setMinValue(0);
        holder.numberPicker.setMaxValue(20);
        holder.numberPicker.setWrapSelectorWheel(true);
        holder.numberPicker.setDescendantFocusability(holder.numberPicker.FOCUS_BLOCK_DESCENDANTS);
        holder.numberPicker.setOnLongPressUpdateInterval(200);
        holder.numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                Log.e("numberp", "old value" + oldVal + "' " + "new val" + newVal + ", " + picker);
                List<Pair<String,String>> cart = ((GlobalCart)context.getApplicationContext()).getCART();


                        if(newVal==0)
                        {
                            for(int i = 0;i<cart.size();i++)
                            {
                                if(cart.get(i).first==listmenu.getName())
                                {
                                    cart.remove(cart.get(i));
                                    break;
                                }
                            }
                            ((GlobalCart)context.getApplicationContext()).setCART(cart);
                        }
                        else
                        {
                            int j=-1;
                            for(int i = 0;i<cart.size();i++)
                            {
                                if(cart.get(i).first==listmenu.getName())
                                {
                                    j=i;
                                    break;
                                }
                            }
                            if(j==-1)
                            {
                                Pair<String,String> food = Pair.create(listmenu.getName(),Integer.toString(newVal));
                                cart.add(food);

                            }
                            else
                            {
                                Pair<String,String> food = Pair.create(listmenu.getName(),Integer.toString(newVal));
                                cart.remove(cart.get(j));
                                cart.add(food);
                            }
                            ((GlobalCart)context.getApplicationContext()).setCART(cart);
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
        public NumberPicker numberPicker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodname=itemView.findViewById(R.id.FoodName_menu);
            imageView=itemView.findViewById(R.id.FoodImage_menu);
            cardView=itemView.findViewById(R.id.Menu_cards);
            numberPicker = itemView.findViewById(R.id.number_picker);
        }
    }


}
