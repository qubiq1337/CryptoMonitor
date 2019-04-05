package com.example.cryptomonitor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.cryptomonitor.R;


public class NavigationBarFragment extends Fragment {
    private NavigationBarListener NavigationBarListener;

    private BottomNavigationView.OnNavigationItemReselectedListener mNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    NavigationBarListener.changeFragment(R.id.top_container, HomeFragment.class.getName());
                    return true;
                case R.id.navigation_favorites:
                    NavigationBarListener.changeFragment(R.id.top_container, FavoritesFragment.class.getName());
                    return true;
                case R.id.navigation_history:
                    NavigationBarListener.changeFragment(R.id.top_container, HistoryFragment.class.getName());
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
        navigation.setOnNavigationItemReselectedListener(mNavigationItemReselectedListener);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        NavigationBarListener = (NavigationBarListener) context;
    }

    public interface NavigationBarListener {
        void changeFragment(int container, String fragment);
    }


}







