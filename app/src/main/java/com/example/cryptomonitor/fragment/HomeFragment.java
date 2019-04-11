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

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.adapters.CoinAdapterHome;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.CoinInfo;
import com.example.cryptomonitor.database.DBHelper;
import com.example.cryptomonitor.model.CoinCryptoCompare;
import com.example.cryptomonitor.model.Datum;
import com.example.cryptomonitor.network_api.Network;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CoinAdapterHome.OnStarClickListener, SwipeRefreshLayout.OnRefreshListener {

    private CoinCryptoCompare mCoinCryptoCompare = new CoinCryptoCompare();
    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;
    private SwipeRefreshLayout mSwipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.rv_coin_itemlist);
        mSwipeRefresh = view.findViewById(R.id.refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);

        mCoinAdapterHome = new CoinAdapterHome(getContext());
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
        startConnectionApi();
        return view;
    }

    public void startConnectionApi() {
        mSwipeRefresh.setRefreshing(true);
        Network.getInstance()
                .getApiCryptoCompare()
                .getTopListData(100, 0, "USD")
                .enqueue(new Callback<CoinCryptoCompare>() {
                    @Override
                    public void onResponse(@NonNull Call<CoinCryptoCompare> call, @NonNull Response<CoinCryptoCompare> response) {
                        mCoinCryptoCompare = response.body();
                        if (mCoinCryptoCompare != null) {
                            List<CoinInfo> coinInfoList = getCoinInfoList(mCoinCryptoCompare);
                            DBHelper.updateDatabase(coinInfoList);
                        }
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefresh.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NonNull Call<CoinCryptoCompare> call, @NonNull Throwable t) {
                        Log.e("ERROR", t.toString());
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefresh.setRefreshing(false);
                            }
                        });
                    }
                });
    }

    private List<CoinInfo> getCoinInfoList(CoinCryptoCompare mCoinCryptoCompare) {
        List<CoinInfo> coinInfoArrayList = new ArrayList<>();
        List<Datum> coinCryptoCompareData = mCoinCryptoCompare.getData();
        CoinInfo coinInfo;
        for (Datum coin : coinCryptoCompareData) {
            String fullName = coin.getCoinInfo().getFullName();
            String shortName = coin.getCoinInfo().getName();
            String price = coin.getDISPLAY().getUSD().getPRICE();
            String imageURL = coin.getCoinInfo().getImageUrl();
            coinInfo = new CoinInfo(fullName, shortName, price, imageURL);
            coinInfoArrayList.add(coinInfo);
        }
        return coinInfoArrayList;
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
    public void onRefresh() {
        startConnectionApi();
    }
}
