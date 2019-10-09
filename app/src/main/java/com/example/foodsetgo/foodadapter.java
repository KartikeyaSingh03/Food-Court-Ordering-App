package com.example.foodsetgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fooditem listmenu=listmenus.get(position);
        holder.foodname.setText(listmenu.getName());


    }

    @Override
    public int getItemCount() {
        return listmenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView foodname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodname=itemView.findViewById(R.id.name);
        }
    }
}
