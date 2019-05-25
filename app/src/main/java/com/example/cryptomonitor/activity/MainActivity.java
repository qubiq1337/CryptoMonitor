package com.example.cryptomonitor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.ToolbarInteractor;
import com.example.cryptomonitor.Utilities;
import com.example.cryptomonitor.briefcase.BriefcaseFragment;
import com.example.cryptomonitor.detailed_coin.DetailedCoin;
import com.example.cryptomonitor.favorite.FavoritesFragment;
import com.example.cryptomonitor.fragment.NavigationBarFragment;
import com.example.cryptomonitor.history.HistoryFragment;
import com.example.cryptomonitor.home.HomeFragment;

import java.util.Objects;

import static com.example.cryptomonitor.detailed_coin.DetailedCoin.EXTRA_INDEX_KEY;
import static com.example.cryptomonitor.detailed_coin.DetailedCoin.EXTRA_POSITION_KEY;

public class MainActivity extends AppCompatActivity implements NavigationBarFragment.NavigationBarListener,
        ToolbarInteractor {


    private static final String SEARCH_TEXT_KEY = "searchKey";
    private static final long ANIM_DURATION = 200;
    private String savedText;
    private SearchView mSearchView;
    private ToolbarInteractor mToolbarInteractor;
    private FrameLayout fragmentContainer;
    private ViewPropertyAnimator animator;
    private boolean isSearchViewExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean mTheme = mPreferences.getBoolean(THEME, false);
        if (mTheme.equals(true)) {
            setTheme(R.style.AppThemeDarkPurple);
        } else {
            setTheme(R.style.AppThemeDark);
        }
        setContentView(R.layout.activity_main);
        fragmentContainer = findViewById(R.id.top_container);
        Toolbar toolbar = findViewById(R.id.home_and_fav_toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            changeFragment(R.id.top_container, HomeFragment.class.getName());
            setupBottomNavBar();
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.top_container);
            if (fragment instanceof ToolbarInteractor) {
                mToolbarInteractor = (ToolbarInteractor) fragment;
            }
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(SEARCH_TEXT_KEY)) {
            isSearchViewExpanded = true;
            savedText = savedInstanceState.getString(SEARCH_TEXT_KEY, "");
        } else {
            isSearchViewExpanded = false;
        }

    }

    @Override
    public void changeFragment(int container, String fragmentName) {

        animator = fragmentContainer
                .animate()
                .setDuration(ANIM_DURATION)
                .alpha(0f)
                .withEndAction(() -> {
                    Fragment fragment;
                    if (fragmentName.equals(HomeFragment.class.getName())) {
                        Objects.requireNonNull(getSupportActionBar()).show();
                        fragment = new HomeFragment();
                    } else if (fragmentName.equals(FavoritesFragment.class.getName())) {
                        Objects.requireNonNull(getSupportActionBar()).show();
                        fragment = new FavoritesFragment();
                    } else if (fragmentName.equals(HistoryFragment.class.getName())) {
                        Objects.requireNonNull(getSupportActionBar()).hide();
                        fragment = new HistoryFragment();
                    } else if (fragmentName.equals(BriefcaseFragment.class.getName())) {
                        Objects.requireNonNull(getSupportActionBar()).hide();
                        fragment = new BriefcaseFragment();
                    } else {
                        Log.e("ERROR", "No such fragment: " + fragmentName);
                        return;
                    }
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(container, fragment, fragmentName);
                    fragmentTransaction.commit();
                    if (fragment instanceof ToolbarInteractor) {
                        mToolbarInteractor = (ToolbarInteractor) fragment;
                    }
                    animator = fragmentContainer
                            .animate()
                            .setDuration(ANIM_DURATION)
                            .alpha(1f);
                    animator.start();
                });
        animator.start();

    }

    private void setupBottomNavBar() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_container, new NavigationBarFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (isSearchViewExpanded) {
            mSearchView.setIconified(true); //сворачивает searchView
        } else
            Utilities.onBackPressed(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_and_fav_menu_toolbar, menu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false); // Dont put app name on bar

        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        ImageView searchIcon = mSearchView.findViewById(R.id.search_button);
        searchIcon.setColorFilter(R.attr.itemIconTint, PorterDuff.Mode.DST);// Replace color of search icon
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchClickListener(this);
        mSearchView.setOnCloseListener(this);
        if (isSearchViewExpanded) {
            mSearchView.setIconified(false);
            mSearchView.setQuery(savedText, false);
        }


//        mSettingsSpinnerItem = menu.findItem(R.id.settings_spinner);
//        Spinner settingsSpinner = (Spinner) mSettingsSpinnerItem.getActionView();
//        settingsSpinner.getBackground().setColorFilter(R.attr.itemIconTint, PorterDuff.Mode.DST); // Replace color of arrow
//        ArrayAdapter<CharSequence> settingsAdapter = ArrayAdapter.createFromResource(this,
//                R.array.settings_spinner, R.layout.spinner_item);
//        settingsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//        settingsSpinner.setAdapter(settingsAdapter);
//        settingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    //Set sort
//                }
//                if (position == 1) {
//                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//                    startActivity(intent);
//                }
//            }
//            //TODO: Refactor this!
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
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
        if (v.getId() == R.id.search) {
            onExpandSearch();
            if (mToolbarInteractor != null)
                mToolbarInteractor.onClick(v);
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
        setGuidelinePercentage(1f);
        isSearchViewExpanded = true;
    }

    private void onClosedSearch() {
        setGuidelinePercentage(0.92f);
        isSearchViewExpanded = false;
    }

    private void setGuidelinePercentage(float v) {
        Guideline guideLine = findViewById(R.id.bottom_nav_guideline);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = v;
        guideLine.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        mSearchView = null;
        mToolbarInteractor = null;
       /* mSwipeRefresh = null;
        networkHelper.getCompositeDisposable().dispose();
        networkHelper = null;*/
        super.onDestroy();

    }

    public void onCoinClicked(String index, int position) {
        Intent intent = new Intent(this, DetailedCoin.class);
        intent.putExtra(EXTRA_INDEX_KEY, index);
        intent.putExtra(EXTRA_POSITION_KEY, position + 1);
        startActivity(intent);
    }
}


