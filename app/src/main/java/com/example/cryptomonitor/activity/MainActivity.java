package com.example.cryptomonitor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.example.cryptomonitor.ExitClass;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.briefcase.BriefcaseFragment;
import com.example.cryptomonitor.favorite.FavoritesFragment;
import com.example.cryptomonitor.fragment.HistoryFragment;
import com.example.cryptomonitor.fragment.NavigationBarFragment;
import com.example.cryptomonitor.home.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarFragment.NavigationBarListener {

    public static final String EXTRA_INDEX_KEY = "INDEX";
    public static final String EXTRA_CURRENCY_KEY = "CURRENCY";
    public static final String EXTRA_POSITION_KEY = "POSITION";
    public static final String THEME = "THEME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(THEME, 0);
        int theme = settings.getInt("theme", R.style.AppThemeDark);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            changeFragment(R.id.top_container, HomeFragment.class.getName());
            changeFragment(R.id.bottom_container, NavigationBarFragment.class.getName());
        }
    }

    @Override
    public void changeFragment(int container, String fragmentName) {
        Fragment fragment;
        if (fragmentName.equals(HomeFragment.class.getName())) {
            changeTheme(R.style.AppThemeDark);
            fragment = new HomeFragment();
        } else if (fragmentName.equals(FavoritesFragment.class.getName())) {
            changeTheme(R.style.AppThemeDark);
            fragment = new FavoritesFragment();
        } else if (fragmentName.equals(HistoryFragment.class.getName())) {
            changeTheme(R.style.HistoryFragmentTheme);
            fragment = new HistoryFragment();
        } else if (fragmentName.equals(NavigationBarFragment.class.getName())) {
            changeTheme(R.style.AppThemeDark);
            fragment = new NavigationBarFragment();
        } else if (fragmentName.equals(BriefcaseFragment.class.getName())) {
            changeTheme(R.style.BriefcaseFragmentTheme);
            fragment = new BriefcaseFragment();
        } else {
            Log.e("ERROR", "No such fragment: " + fragmentName);
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.commit();
    }

    private void changeTheme(int theme) {
        SharedPreferences settings = getSharedPreferences(THEME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("theme", theme);
        editor.apply();
        recreate();
    }

    @Override
    public void onBackPressed() {
            ExitClass.onBackPressed(this);
    }

//        mSettingsItem = menu.findItem(R.id.sort);
//        mSettingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                View viewSettings = findViewById(R.id.menu_one);
//                PopupMenu popupMenu = new PopupMenu(MainActivity.this, viewSettings);
//                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//                popupMenu.show();
//
//                return true;
//            }
//        });

    public void onCoinClicked(String index, int position,String currency) {
        Intent intent = new Intent(this, DetailedCoin.class);
        intent.putExtra(EXTRA_INDEX_KEY, index);
        intent.putExtra(EXTRA_CURRENCY_KEY, currency);
        intent.putExtra(EXTRA_POSITION_KEY, position + 1);
        Log.e("OnCoinClickMain",index+" "+position);
        startActivity(intent);
    }
}


