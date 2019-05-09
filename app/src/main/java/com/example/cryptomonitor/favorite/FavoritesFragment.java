package com.example.cryptomonitor.favorite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.Message;


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
        mViewModel.getFavoriteCoinsLiveData().observe(this, coinInfoList -> mCoinAdapterHome.setData(coinInfoList));
        mViewModel.getEventLiveData().observe(this, eventObserver);
        return view;
    }

    private Observer<Event> eventObserver = event -> {
        if (!event.isHandled()) {
            if (event instanceof Message) {
                Message message = (Message) event;
                Toast.makeText(getContext(), message.getMessageText(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onStarClick(CoinInfo clickedCoinInfo) {
        mViewModel.onStarClicked(clickedCoinInfo);
    }
}
