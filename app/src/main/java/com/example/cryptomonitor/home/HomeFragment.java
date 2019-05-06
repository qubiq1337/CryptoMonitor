package com.example.cryptomonitor.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.ToolbarInteractor;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.Message;

import java.util.List;


public class HomeFragment extends Fragment implements CoinAdapterHome.OnStarClickListener,
        ToolbarInteractor,
        CoinAdapterHome.OnEndReachListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String SEARCH_MODE_KEY = "modeKey";
    public static final String TAG = "MyLogs";
    private boolean isSearchViewExpanded;
    private String mCurrency;
    private HomeViewModel mHomeViewModel;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.rv_coin_itemlist);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

        mCoinAdapterHome = new CoinAdapterHome(getContext());
        mCoinAdapterHome.setup(this);

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);
        mRecyclerView.setAdapter(mCoinAdapterHome);

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.getCoinsLiveData().observe(this, listObserver);
        mHomeViewModel.getEventLiveData().observe(this, eventObserver);
        mHomeViewModel.getSwipeRefreshLiveData().observe(this, swipeRefreshObserver);
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
        mSwipeRefresh = null;
        super.onDestroy();
    }

    private Observer<List<CoinInfo>> listObserver = coinInfoList -> {
        mCoinAdapterHome.setData(coinInfoList);
    };

    private Observer<Event> eventObserver = event -> {
        if (event instanceof Message) {
            Message message = (Message) event;
            Toast.makeText(getContext(), message.getMessageText(), Toast.LENGTH_SHORT).show();
        }
    };

    private Observer<Boolean> swipeRefreshObserver = isRefreshing -> {
        if (isRefreshing)
            mSwipeRefresh.setRefreshing(true);
        else
            mSwipeRefresh.setRefreshing(false);
    };

    @Override
    public void onEndReach() {
        mHomeViewModel.onEndReached();
    }

    @Override
    public void setCurrency(String currency) {
        mCurrency = currency;
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mHomeViewModel.refresh(mCurrency);
    }
}
