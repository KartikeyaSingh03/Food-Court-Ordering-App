package com.example.foodsetgo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class YourOrdersFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adap;
    RecyclerView.LayoutManager layoutManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        final View v= inflater.inflate(R.layout.fragment_home, container, false);


        return v;
    }

}
