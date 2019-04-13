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
import com.example.cryptomonitor.adapters.CoinAdapterHome;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.database.DBHelper;
import com.example.cryptomonitor.view_models.FavoriteViewModel;

import java.util.List;


public class FavoritesFragment extends Fragment implements CoinAdapterHome.OnStarClickListener {

    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        mRecyclerView = view.findViewById(R.id.rv_favorite_coins);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);
        mCoinAdapterHome = new CoinAdapterHome(getContext());
        mCoinAdapterHome.setOnStarClickListener(this);
        mRecyclerView.setAdapter(mCoinAdapterHome);

        FavoriteViewModel viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        viewModel.getFavoriteCoinsLiveData().observe(this, new Observer<List<CoinInfo>>() {
            @Override
            public void onChanged(@Nullable List<CoinInfo> coinInfoList) {
                mCoinAdapterHome.setCoinData(coinInfoList);
            }
        });
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
}
