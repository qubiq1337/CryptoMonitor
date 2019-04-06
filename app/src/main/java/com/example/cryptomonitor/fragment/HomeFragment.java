package com.example.cryptomonitor.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.adapters.CoinAdapterHome;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.CoinInfo;
import com.example.cryptomonitor.database.CoinInfoDao;
import com.example.cryptomonitor.database.UpdateOperation;
import com.example.cryptomonitor.model.CoinCryptoCompare;
import com.example.cryptomonitor.model.Datum;
import com.example.cryptomonitor.network_api.Network;
import com.example.cryptomonitor.network_api.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CoinAdapterHome.OnStarClickListener, SwipeRefreshLayout.OnRefreshListener, CoinAdapterHome.OnLoadListener {

    private CoinCryptoCompare mCoinCryptoCompare = new CoinCryptoCompare();
    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;
    private SwipeRefreshLayout mSwipeRefresh;
    private NetworkHelper networkHelper = new NetworkHelper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        mRecyclerView = view.findViewById(R.id.rv_coin_itemlist);
        mSwipeRefresh = view.findViewById(R.id.refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);
        mCoinAdapterHome = new CoinAdapterHome(getContext(), mRecyclerView);
        mCoinAdapterHome.setOnLoadListener(this);
        mCoinAdapterHome.setOnStarClickListener(this);
        mRecyclerView.setAdapter(mCoinAdapterHome);
        Disposable getDataFromDB = App.getDatabase().coinInfoDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CoinInfo>>() {
                    @Override
                    public void accept(final List<CoinInfo> coinInfoList) {
                        mCoinAdapterHome.setCoinData(coinInfoList);
                    }
                });
        networkHelper.start("USD");
        return view;
    }

    @Override
    public void onStarClick(int position) {
        CoinInfo clickedCoinInfo = mCoinAdapterHome.getCoinData().get(position);
        if (clickedCoinInfo.isFavorite())
            clickedCoinInfo.setFavorite(false);
        else
            clickedCoinInfo.setFavorite(true);
        Observable.fromCallable(new UpdateOperation(clickedCoinInfo))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void onRefresh() {
        networkHelper.start("USD");
        if (networkHelper.isRefreshing() == false) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void loadMore(int page) {
        //networkHelper.loadNextCoin(page,"USD");
        Toast.makeText(getActivity(), "page ", Toast.LENGTH_SHORT).show();

    }
}
