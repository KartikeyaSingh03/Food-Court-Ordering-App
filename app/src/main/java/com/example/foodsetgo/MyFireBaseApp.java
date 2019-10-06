package com.example.foodsetgo;

import com.google.firebase.database.FirebaseDatabase;

public class MyFireBaseApp extends android.app.Application {
    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
