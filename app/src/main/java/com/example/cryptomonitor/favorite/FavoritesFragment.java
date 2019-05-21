package com.example.cryptomonitor.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.ToolbarInteractor;
import com.example.cryptomonitor.activity.MainActivity;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.Message;

import java.util.Objects;


public class FavoritesFragment extends Fragment implements FavoriteCoinAdapter.OnStarClickListener,
        FavoriteCoinAdapter.OnCoinClickListener,
        ToolbarInteractor, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FavoriteCoinAdapter mCoinAdapterHome;
    private FavoriteViewModel mViewModel;
    private String mCurrency;


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

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        mViewModel.getFavoriteCoinsLiveData().observe(this, coinInfoList -> mCoinAdapterHome.setData(coinInfoList));
        mViewModel.getEventLiveData().observe(this, eventObserver);
        mViewModel.getIsRefreshLiveData().observe(this, swipeRefreshObserver);
        return view;
    }

    private Observer<Event> eventObserver = event -> {
        if (event != null && !event.isHandled()) {
            if (event instanceof Message) {
                Message message = (Message) event;
                Toast.makeText(getContext(), message.getMessageText(), Toast.LENGTH_SHORT).show();
                message.handled();
            }
        }
    };

    private Observer<Boolean> swipeRefreshObserver = isRefreshing -> {
        if (isRefreshing)
            mSwipeRefreshLayout.setRefreshing(true);
        else
            mSwipeRefreshLayout.setRefreshing(false);
    };

    @Override
    public void onStarClick(CoinInfo clickedCoinInfo) {
        mViewModel.onStarClicked(clickedCoinInfo);
    }

    @Override
    public void onCoinClick(String index, int position) {
        ((MainActivity) Objects.requireNonNull(getActivity())).onCoinClicked(index, position);
    }

    @Override
    public void setCurrency(String currency) {
        mCurrency = currency;
        onRefresh();
    }

    @Override
    public void onClick(View v) {
        mViewModel.onSearchClicked();
    }

    @Override
    public boolean onClose() {
        mViewModel.onSearchDeactivated();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mViewModel.onTextChanged(newText);
        return false;
    }

    @Override
    public void onRefresh() {
        if (mViewModel != null)
            mViewModel.onRefresh(mCurrency);
    }
}
