package com.example.foodsetgo.Owners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodsetgo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Res_Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res__home);

        BottomNavigationView bottomNav=findViewById(R.id.res_bottom_navbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,
        new ListFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment=null;
                    switch (menuItem.getItemId())
                    {
                        case R.id.owners_list:
                        selectedFragment=new ListFragment();
                        break;
                        case R.id.owners_profile:
                        selectedFragment=new OwnersProfileFragment();
                        break;
                        case R.id.owner_orders:
                        selectedFragment = new Owners_Orders_fragment();
                        break;



                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                            selectedFragment).commit();
                    return true;
                }
            };
}
