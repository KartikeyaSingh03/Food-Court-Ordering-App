package com.example.foodsetgo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrl = new ArrayList<>();
    View Viewroot;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        Viewroot=inflater.inflate(R.layout.fragment_home, container, false);
        initImageBitmaps();
        return Viewroot;
    }

    private void initImageBitmaps(){
        mImageUrl.add("https://www.gstatic.com/webp/gallery/1.jpg");
        mNames.add("Norway");

        mImageUrl.add("https://www.gstatic.com/webp/gallery/2.jpg");
        mNames.add("Kayaker");

        mImageUrl.add("https://www.gstatic.com/webp/gallery/4.jpg");
        mNames.add("Cherry");
        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = Viewroot.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mImageUrl,mNames,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
