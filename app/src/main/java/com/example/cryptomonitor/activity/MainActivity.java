package com.example.cryptomonitor.activity;

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
import android.widget.Spinner;

import com.example.cryptomonitor.ExitClass;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.fragment.BriefcaseFragment;
import com.example.cryptomonitor.fragment.FavoritesFragment;
import com.example.cryptomonitor.fragment.HistoryFragment;
import com.example.cryptomonitor.fragment.HomeFragment;
import com.example.cryptomonitor.fragment.NavigationBarFragment;
import com.example.cryptomonitor.network_api.NetworkHelper;

public class MainActivity extends AppCompatActivity implements NavigationBarFragment.NavigationBarListener,
        SwipeRefreshLayout.OnRefreshListener,
        NetworkHelper.OnChangeRefreshingListener {

    private String mCurrency;
    private SwipeRefreshLayout mSwipeRefresh;
    private NetworkHelper networkHelper = new NetworkHelper(this);
    private Fragment mCurrentFragment;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(R.id.top_container, HomeFragment.class.getName());
        changeFragment(R.id.bottom_container, NavigationBarFragment.class.getName());
        networkHelper.setOnChangeRefreshingListener(this);
        mSwipeRefresh = findViewById(R.id.refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
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
        } else if (fragmentName.equals(BriefcaseFragment.class.getName())) {
            fragment = new BriefcaseFragment();
        } else {
            Log.e("ERROR", "No such fragment: " + fragmentName);
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.commit();
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) fragment);
            mSearchView.setOnSearchClickListener((View.OnClickListener) fragment);
            mSearchView.setOnCloseListener((SearchView.OnCloseListener) fragment);
        }
    }

    @Override
    public void onBackPressed() {
        ExitClass.onBackPressed(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_layout, menu);
        MenuItem spinnerItem = menu.findItem(R.id.action_bar_spinner);
        Spinner spinner = (Spinner) spinnerItem.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        return true;
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(true);
        networkHelper.refreshCoins(mCurrency);
    }

    @Override
    public void stopRefreshing(boolean isSuccessful) {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void startRefreshing() {

    }
}


