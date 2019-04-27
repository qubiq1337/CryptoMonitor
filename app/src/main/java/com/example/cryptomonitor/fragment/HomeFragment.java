package com.example.cryptomonitor.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.ToolbarInteractor;
import com.example.cryptomonitor.adapters.CoinAdapterHome;
import com.example.cryptomonitor.database.CoinDataHelper;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.view_models.HomeViewModel;
import com.example.cryptomonitor.view_models.SearchViewModel;

import java.util.List;


public class HomeFragment extends Fragment implements CoinAdapterHome.OnStarClickListener, ToolbarInteractor, CoinAdapterHome.OnEndReachListener {

    private static final String SEARCH_MODE_KEY = "modeKey";
    public static final String TAG = "MyLogs";
    private boolean isSearchViewExpanded;

    private Observer<List<CoinInfo>> listObserver = new Observer<List<CoinInfo>>() {
        @Override
        public void onChanged(@Nullable List<CoinInfo> coinInfos) {
            mCoinAdapterHome.setData(coinInfos);
        }
    };
    private HomeViewModel mHomeViewModel;
    private SearchViewModel mSearchViewModel;
    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            isSearchViewExpanded = savedInstanceState.getBoolean(SEARCH_MODE_KEY, false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.rv_coin_itemlist);

        mCoinAdapterHome = new CoinAdapterHome(getContext());
        mCoinAdapterHome.setup(this);

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);
        mRecyclerView.setAdapter(mCoinAdapterHome);

        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        if (isSearchViewExpanded)
            mSearchViewModel.getSearchLiveData().observe(this, listObserver);
        else
            mHomeViewModel.getAllCoinsLiveData().observe(this, listObserver);
        return view;
    }

    @Override
    public void onStarClick(CoinInfo clickedCoinInfo) {
        if (clickedCoinInfo.isFavorite())
            clickedCoinInfo.setFavorite(false);
        else
            clickedCoinInfo.setFavorite(true);
        CoinDataHelper.updateCoin(clickedCoinInfo);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String currentText) {
        mSearchViewModel.onTextChanged(currentText);
        return false;
    }

    @Override
    public void onClick(View v) {
        mHomeViewModel.getAllCoinsLiveData().removeObservers(this);
        mSearchViewModel.getSearchLiveData().observe(this, listObserver);
        isSearchViewExpanded = true;
    }

    @Override
    public boolean onClose() {
        mSearchViewModel.getSearchLiveData().removeObservers(this);
        mHomeViewModel.getAllCoinsLiveData().observe(this, listObserver);
        isSearchViewExpanded = false;
        return false;
    }

    @Override
    public void onDestroy() {
        mSearchViewModel = null;
        mHomeViewModel = null;
        mCoinAdapterHome = null;
        mRecyclerView = null;
        listObserver = null;
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SEARCH_MODE_KEY, isSearchViewExpanded);
    }

    @Override
    public void onEndReach() {
        mHomeViewModel.onEndReached();
    }
}
