package com.example.cryptomonitor.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.ToolbarInteractor;
import com.example.cryptomonitor.activity.MainActivity;
import com.example.cryptomonitor.database.coins.CoinInfo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.Message;

import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements CoinAdapterHome.OnStarClickListener,
        ToolbarInteractor,
        SwipeRefreshLayout.OnRefreshListener,
        CoinAdapterHome.OnCoinClickListener {

    private HomeViewModel mHomeViewModel;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;
    private Observer<List<CoinInfo>> listObserver = coinInfoList -> {
        if (coinInfoList != null)
            mCoinAdapterHome.setList(coinInfoList);
        else
            mCoinAdapterHome.showMode();
    };
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
            mSwipeRefresh.setRefreshing(true);
        else
            mSwipeRefresh.setRefreshing(false);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.rv_coin_itemlist);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.dark1, R.color.dark2, R.color.dark3);

        mCoinAdapterHome = new CoinAdapterHome(getContext());
        mCoinAdapterHome.setup(this);

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);
        mRecyclerView.setAdapter(mCoinAdapterHome);

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.getSearchModeLiveData().observe(this, listObserver);
        mHomeViewModel.getEventLiveData().observe(this, eventObserver);
        mHomeViewModel.getSwipeRefreshLiveData().observe(this, swipeRefreshObserver);
        mHomeViewModel.refresh();
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
        if (v.getId() == R.id.search) {
            mHomeViewModel.onSearchClicked();
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


    @Override
    public void onRefresh() {
        if (mHomeViewModel != null)
            mHomeViewModel.refresh();
    }

    @Override
    public void onCoinClick(String index, int position) {
        ((MainActivity) Objects.requireNonNull(getActivity())).onCoinClicked(index, position);
    }
}
