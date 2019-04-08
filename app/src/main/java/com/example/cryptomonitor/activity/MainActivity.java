package com.example.cryptomonitor.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.cryptomonitor.ExitClass;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.fragment.FavoritesFragment;
import com.example.cryptomonitor.fragment.HistoryFragment;
import com.example.cryptomonitor.fragment.HomeFragment;
import com.example.cryptomonitor.fragment.NavigationBarFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarFragment.NavigationBarListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(R.id.top_container, HomeFragment.class.getName());
        changeFragment(R.id.bottom_container, NavigationBarFragment.class.getName());
    }


    @Override
    public void changeFragment(int container, String fragmentName) {
        Fragment fragment;
        if (fragmentName.equals(HomeFragment.class.getName())) {
            fragment = new HomeFragment();
        } else if (fragmentName.equals(FavoritesFragment.class.getName())) {
            fragment = new FavoritesFragment();
        } else if (fragmentName.equals(HistoryFragment.class.getName())) {
            fragment = new HistoryFragment();
        } else if (fragmentName.equals(NavigationBarFragment.class.getName())) {
            fragment = new NavigationBarFragment();
        } else {
            Log.e("ERROR", "No such fragment: " + fragmentName);
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        ExitClass.onBackPressed(this);
    }
}


