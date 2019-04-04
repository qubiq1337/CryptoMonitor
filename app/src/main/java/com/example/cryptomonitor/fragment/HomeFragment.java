package com.example.cryptomonitor.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.cryptomonitor.database.CoinInfo;
import com.example.cryptomonitor.model.CoinCryptoCompare;
import com.example.cryptomonitor.network_api.Network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private CoinCryptoCompare mCoinCryptoCompare = new CoinCryptoCompare();
    private RecyclerView mRecyclerView;
    private CoinAdapterHome mCoinAdapterHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        mRecyclerView = view.findViewById(R.id.rv_coin_itemlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCoinAdapterHome = new CoinAdapterHome(getContext());
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_fall_down);
        mRecyclerView.setLayoutAnimation(animation);
        Log.d("MyLogs", "onCreateView: " + Thread.currentThread().getId());
        mRecyclerView.setAdapter(mCoinAdapterHome);
        startConnectionApi();
        return view;
    }

    public void startConnectionApi() {
        Network.getInstance()
                .getApiCryptoCompare()
                .getTopListData(100, 0, "USD")
                .enqueue(new Callback<CoinCryptoCompare>() {
                    @Override
                    public void onResponse(@NonNull Call<CoinCryptoCompare> call, @NonNull Response<CoinCryptoCompare> response) {
                        mCoinCryptoCompare = response.body();
                        ArrayList<CoinInfo> coinInfoList = new ArrayList<>();
                        Log.d("MyLogs", "onResponse: " + Thread.currentThread().getId());
                        if (mCoinCryptoCompare != null)
                            coinInfoList = new ArrayList<>();
                        ((CoinAdapterHome) mRecyclerView.getAdapter()).setCoinData(coinInfoList);
                    }

                   /* private ArrayList<CoinInfo> getCoinInfoList(CoinCryptoCompare mCoinCryptoCompare) {
                        ArrayList<CoinInfo> coinInfoArrayList = new ArrayList<>();
                        CoinInfo coinInfo;
                        for (int i = 0; i < mCoinCryptoCompare.getData().size(); i++) {
                            String fullName = mCoinCryptoCompare.getData().get(i).getCoinInfo().getFullName();
                            String shortName = mCoinCryptoCompare.getData().get(i).getCoinInfo().getName();
                            String price = mCoinCryptoCompare.getData().get(i).getDISPLAY().getUSD().getPRICE();
                            String imageURL = mCoinCryptoCompare.getData().get(i).getCoinInfo().getImageUrl();
                            coinInfo = new CoinInfo(fullName, shortName, price, imageURL);
                            coinInfoArrayList.add(coinInfo);
                        }
                        return coinInfoArrayList;
                    }*/

                    @Override
                    public void onFailure(@NonNull Call<CoinCryptoCompare> call, @NonNull Throwable t) {
                        Log.e("ERROR", t.toString());
                    }
                });
    }
}
