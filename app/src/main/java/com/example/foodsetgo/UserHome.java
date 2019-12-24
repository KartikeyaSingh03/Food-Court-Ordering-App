package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class UserHome extends AppCompatActivity {
    private HashMap<String , ArrayList<Pair<String,String>>> CART;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selctedFrag =null;
            switch(menuItem.getItemId()){
                case R.id.nav_home:
                    selctedFrag = new HomeFragment();
                    break;
                case R.id.nav_cart:
                    selctedFrag = new TimelineUser();
                    break;
                case R.id.nav_profile:
                    selctedFrag = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selctedFrag).commit();
            return true;
        }

    };

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }
}
