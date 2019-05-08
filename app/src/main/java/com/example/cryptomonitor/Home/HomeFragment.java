package com.example.cryptomonitor.Home;

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
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;


public class HomeFragment extends Fragment implements CoinAdapterHome.OnStarClickListener, ToolbarInteractor, CoinAdapterHome.OnEndReachListener {

    public static final String TAG = "MyLogs";
    private static final String SEARCH_MODE_KEY = "modeKey";
    private boolean isSearchViewExpanded;
    private Observer<Boolean> searchModeObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean aBoolean) {

        }
    };
    private HomeViewModel mHomeViewModel;
    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;
    private Observer<List<CoinInfo>> listObserver = new Observer<List<CoinInfo>>() {
        @Override
        public void onChanged(@Nullable List<CoinInfo> coinInfos) {
            mCoinAdapterHome.setData(coinInfos);
        }
    };

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

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.getCoinsLiveData().observe(this, listObserver);
        return view;
    }

    @Override
    public void onStarClick(CoinInfo clickedCoinInfo) {
        mHomeViewModel.onStarClicked(clickedCoinInfo);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String currentText) {
        mHomeViewModel.onTextChanged(currentText);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                mHomeViewModel.onSearchClicked();
                break;
        }
    }

    @Override
    public boolean onClose() {
        mHomeViewModel.onSearchDeactivated();
        return false;
    }

    @Override
    public void onDestroy() {
        mHomeViewModel = null;
        mCoinAdapterHome = null;
        mRecyclerView = null;
        listObserver = null;
        super.onDestroy();
    }

    @Override
    public void onEndReach() {
        mHomeViewModel.onEndReached();
    }
}
