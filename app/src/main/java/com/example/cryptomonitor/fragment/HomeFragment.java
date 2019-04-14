package com.example.cryptomonitor.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.adapters.CoinAdapterHome;
import com.example.cryptomonitor.database.DBHelper;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.network_api.NetworkHelper;
import com.example.cryptomonitor.view_models.HomeViewModel;
import com.example.cryptomonitor.view_models.SearchViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements CoinAdapterHome.OnStarClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        NetworkHelper.OnChangeRefreshingListener, 
        SearchView.OnQueryTextListener, View.OnClickListener, SearchView.OnCloseListener {

    public static final String TAG = "MyLogs";
    private Observer <List<CoinInfo>> listObserver = new Observer<List<CoinInfo>>() {
        @Override
        public void onChanged(@Nullable List<CoinInfo> coinInfos) {
            mCoinAdapterHome.setCoinData(coinInfos);
        }
    };
    private HomeViewModel mHomeViewModel;
    private SearchViewModel mSearchViewModel;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;
    private SwipeRefreshLayout mSwipeRefresh;
    private NetworkHelper mNetworkHelper = new NetworkHelper();
    private SearchView mSearchView;
    private Spinner mSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = view.findViewById(R.id.rv_coin_itemlist);
        mSwipeRefresh = view.findViewById(R.id.refresh);
        mToolbar = getActivity().findViewById(R.id.toolbar);

        mSearchView = mToolbar.findViewById(R.id.search_bar);
        mSpinner = mToolbar.findViewById(R.id.action_bar_spinner);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchClickListener(this);
        mSearchView.setOnCloseListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCoinAdapterHome = new CoinAdapterHome(getContext());
        mCoinAdapterHome.setOnStarClickListener(this);
        mRecyclerView.setAdapter(mCoinAdapterHome);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);

        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.getAllCoinsLiveData().observe(this, listObserver);

        mNetworkHelper.setOnChangeRefreshingListener(this);
        mNetworkHelper.start("USD");
        return view;
    }

    @Override
    public void onStarClick(int position) {
        CoinInfo clickedCoinInfo = mCoinAdapterHome.getCoinData().get(position);
        if (clickedCoinInfo.isFavorite())
            clickedCoinInfo.setFavorite(false);
        else
            clickedCoinInfo.setFavorite(true);
        DBHelper.updateCoin(clickedCoinInfo);
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
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(true);
        mNetworkHelper.start("USD");
    }

    @Override
    public void stopRefreshing(boolean isSuccess) {
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String currentText) {
        mSearchViewModel.changeSearchList(currentText);
        return false;
    }

    @Override
    public void onClick(View v) {
        mHomeViewModel.getAllCoinsLiveData().removeObservers(this);
        mSearchViewModel.getSearchCoinsLiveData().observe(this, listObserver);
    }

    @Override
    public boolean onClose() {
        mSearchViewModel.getSearchCoinsLiveData().removeObservers(this);
        mHomeViewModel.getAllCoinsLiveData().observe(this, listObserver);
        return false;
    }
}
