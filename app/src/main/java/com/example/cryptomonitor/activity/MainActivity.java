package com.example.cryptomonitor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.cryptomonitor.ExitClass;
import com.example.cryptomonitor.Favorite.FavoritesFragment;
import com.example.cryptomonitor.Home.CoinAdapterHome;
import com.example.cryptomonitor.Home.HomeFragment;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.ToolbarInteractor;
import com.example.cryptomonitor.fragment.BriefcaseFragment;
import com.example.cryptomonitor.fragment.HistoryFragment;
import com.example.cryptomonitor.fragment.NavigationBarFragment;
import com.example.cryptomonitor.network_api.NetworkHelper;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationBarFragment.NavigationBarListener,
        SwipeRefreshLayout.OnRefreshListener,
        NetworkHelper.OnChangeRefreshingListener,
        ToolbarInteractor,
        CoinAdapterHome.OnCoinClickListener {

    public static final String EXTRA_INDEX_KEY = "INDEX";
    public static final String EXTRA_CURRENCY_KEY = "CURRENCY";
    public static final String EXTRA_POSITION_KEY = "POSITION";
    public static final String THEME = "THEME";
    private static final String SEARCH_TEXT_KEY = "searchKey";
    private String mCurrency;
    private String savedText;
    private SwipeRefreshLayout mSwipeRefresh;
    private NetworkHelper networkHelper = new NetworkHelper(this);
    private SearchView mSearchView;
    private MenuItem mSpinnerItem;
    private MenuItem mSettingsItem;
    private ToolbarInteractor mToolbarInteractor;
    private boolean isSearchViewExpanded;

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
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.top_container);
            if (fragment instanceof ToolbarInteractor)
                mToolbarInteractor = (ToolbarInteractor) fragment;
        }
        mSwipeRefresh = findViewById(R.id.refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.greenColor, R.color.redColor, R.color.blue_color_selected);
        if (savedInstanceState != null && savedInstanceState.containsKey(SEARCH_TEXT_KEY)) {
            isSearchViewExpanded = true;
            savedText = savedInstanceState.getString(SEARCH_TEXT_KEY, "");
        } else {
            isSearchViewExpanded = false;
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
        if (fragment instanceof ToolbarInteractor)
            mToolbarInteractor = (ToolbarInteractor) fragment;
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
        if (isSearchViewExpanded) {
            mSearchView.setIconified(true); //сворачивает searchView
        } else
            ExitClass.onBackPressed(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_layout, menu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false); // Dont put app name on bar
        mSpinnerItem = menu.findItem(R.id.action_bar_spinner);
        Spinner spinner = (Spinner) mSpinnerItem.getActionView();
        spinner.getBackground().setColorFilter(R.attr.itemIconTint, PorterDuff.Mode.DST); // Replace color of arrow
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] spinnerArray = getResources().getStringArray(R.array.spinner);
                mCurrency = spinnerArray[position];
                networkHelper.refreshCoins(mCurrency);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        ImageView searchIcon = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchIcon.setColorFilter(R.attr.itemIconTint, PorterDuff.Mode.DST);// Replace color of search icon


        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchClickListener(this);
        mSearchView.setOnCloseListener(this);
        if (isSearchViewExpanded) {
            mSearchView.setIconified(false);
            mSearchView.setQuery(savedText, false);
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

        return true;
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(true);
        networkHelper.refreshCoins(mCurrency);
    }

    @Override
    public void stopRefreshing(boolean isSuccessful) {
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void startRefreshing() {
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });
    }

    @Override
    public boolean onClose() {
        onClosedSearch();
        if (mToolbarInteractor != null)
            mToolbarInteractor.onClose();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (mToolbarInteractor != null)
            mToolbarInteractor.onQueryTextSubmit(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (mToolbarInteractor != null)
            mToolbarInteractor.onQueryTextChange(s);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                onExpandSearch();
                if (mToolbarInteractor != null)
                    mToolbarInteractor.onClick(v);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isSearchViewExpanded) {
            outState.putString(SEARCH_TEXT_KEY, mSearchView.getQuery().toString());
        }
    }

    private void onExpandSearch() {
        isSearchViewExpanded = true;
        mSpinnerItem.setVisible(false);
        getSupportActionBar().setTitle("");
    }

    private void onClosedSearch() {
        isSearchViewExpanded = false;
        mSpinnerItem.setVisible(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
    }

    @Override
    protected void onDestroy() {
        mSearchView = null;
        mSpinnerItem = null;
        mToolbarInteractor = null;
        mSwipeRefresh = null;
        networkHelper.getCompositeDisposable().dispose();
        networkHelper = null;
        super.onDestroy();

    }

    @Override
    public void goToDetailedCoin(String index, int position) {
        Intent intent = new Intent(this, DetailedCoin.class);
        intent.putExtra(EXTRA_INDEX_KEY, index);
        intent.putExtra(EXTRA_CURRENCY_KEY, mCurrency);
        intent.putExtra(EXTRA_POSITION_KEY, position + 1);
        startActivity(intent);
        finish();
    }
}


