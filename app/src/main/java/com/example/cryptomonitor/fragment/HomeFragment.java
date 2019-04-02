package com.example.cryptomonitor;

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

import com.example.cryptomonitor.model.CoinCryptoCompare;
import com.example.cryptomonitor.network_api.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private CoinCryptoCompare mCoinCryptoCompare = new CoinCryptoCompare();
    private RecyclerView recyclerView;
    private CoinAdapterHome coinAdapterHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.rv_coin_itemlist);
        startConnectionApi();
        return view;
    }

    public void startConnectionApi() {

        Network.getmInstance()
                .getApiCryptoCompare()
                .getTopListData(100, 0, "USD")
                .enqueue(new Callback<CoinCryptoCompare>() {
                    @Override
                    public void onResponse(Call<CoinCryptoCompare> call, Response<CoinCryptoCompare> response) {
                        mCoinCryptoCompare = response.body();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        coinAdapterHome = new CoinAdapterHome(mCoinCryptoCompare, getActivity());
                        recyclerView.setAdapter(coinAdapterHome);
                    }

                    @Override
                    public void onFailure(Call<CoinCryptoCompare> call, Throwable t) {
                        Log.e("ERROR", t.toString());
                    }
                });
    }
}
