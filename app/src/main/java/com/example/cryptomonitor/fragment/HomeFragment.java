package com.example.cryptomonitor.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.ToolbarInteractor;
import com.example.cryptomonitor.adapters.CoinAdapterHome;
import com.example.cryptomonitor.database.DBHelper;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.view_models.HomeViewModel;
import com.example.cryptomonitor.view_models.SearchViewModel;

import java.util.List;


public class HomeFragment extends Fragment implements CoinAdapterHome.OnStarClickListener, ToolbarInteractor {

    public static final String TAG = "MyLogs";
    private Observer<List<CoinInfo>> listObserver = new Observer<List<CoinInfo>>() {
        @Override
        public void onChanged(@Nullable List<CoinInfo> coinInfos) {
            mCoinAdapterHome.setCoinData(coinInfos);
        }
    };
    private HomeViewModel mHomeViewModel;
    private SearchViewModel mSearchViewModel;
    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;

    private enum MODE {Search, Default}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.rv_coin_itemlist);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCoinAdapterHome = new CoinAdapterHome(getContext());
        mCoinAdapterHome.setOnStarClickListener(this);

        mRecyclerView.setAdapter(mCoinAdapterHome);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);

        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.getAllCoinsLiveData().observe(this, listObserver);
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
