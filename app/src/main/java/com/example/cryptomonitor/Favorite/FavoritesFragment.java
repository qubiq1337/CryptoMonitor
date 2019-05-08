package com.example.cryptomonitor.Favorite;

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
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;


public class FavoritesFragment extends Fragment implements FavoriteCoinAdapter.OnStarClickListener {

    private RecyclerView mRecyclerView;
    private FavoriteCoinAdapter mCoinAdapterHome;
    private FavoriteViewModel mViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        mRecyclerView = view.findViewById(R.id.rv_favorite_coins);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);
        mCoinAdapterHome = new FavoriteCoinAdapter(getContext());
        mCoinAdapterHome.setup(this);
        mRecyclerView.setAdapter(mCoinAdapterHome);

        mViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        mViewModel.getFavoriteCoinsLiveData().observe(this, new Observer<List<CoinInfo>>() {
            @Override
            public void onChanged(@Nullable List<CoinInfo> coinInfoList) {
                mCoinAdapterHome.setData(coinInfoList);
            }
        });
        return view;
    }

    @Override
    public void onStarClick(CoinInfo clickedCoinInfo) {
        mViewModel.onStarClicked(clickedCoinInfo);
    }
}
