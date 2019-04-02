package com.example.cryptomonitor.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.cryptomonitor.HomeFragment;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.fragment.NavigationBarFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeFragment homeFragment = new HomeFragment();
        NavigationBarFragment navigationBarFragment = new NavigationBarFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.top_container, homeFragment);
        fragmentTransaction.replace(R.id.bottom_container, navigationBarFragment);
        fragmentTransaction.commit();
    }
}


