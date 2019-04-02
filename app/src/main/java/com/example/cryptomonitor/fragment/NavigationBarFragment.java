package com.example.cryptomonitor.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.cryptomonitor.HomeFragment;
import com.example.cryptomonitor.R;


public class NavigationBarFragment extends Fragment {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeFragment homeFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.top_container, homeFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_favorites:
                    FavoritesFragment favoritesFragment = new FavoritesFragment();
                    fragmentTransaction.replace(R.id.top_container, favoritesFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_history:
                    HistoryFragment historyFragment = new HistoryFragment();
                    fragmentTransaction.replace(R.id.top_container, historyFragment);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation_bar, container, false);
        BottomNavigationView navigation = rootView.findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        return rootView;
    }
}







