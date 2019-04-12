package com.example.cryptomonitor.network_api;

import android.util.Log;

import com.example.cryptomonitor.database.CoinInfo;
import com.example.cryptomonitor.database.DBHelper;
import com.example.cryptomonitor.model_coinmarket_cup.CoinMarketCup;
import com.example.cryptomonitor.model_coinmarket_cup.Datum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NetworkHelper {

    private final int start_limit = 5000;
    private final int start_page = 1;
    private OnChangeRefreshingListener refreshingListener;
    public static boolean connection_failed;

    public void start(String curency) {
        loadCoins(curency);
    }

    private void loadCoins(String curency) {
        Network.getInstance()
                .getApiCoinMarketCup()
                .getAllCoinData(start_page, start_limit, curency)
                .enqueue(new Callback<CoinMarketCup>() {
                    @Override
                    public void onResponse(Call<CoinMarketCup> call, Response<CoinMarketCup> response) {
                        if (response.body() != null) {
                            DBHelper.updateDatabase(getCoinInfoList(response.body()));
                            refreshingListener.stopRefreshing();
                            connection_failed = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<CoinMarketCup> call, Throwable t) {
                        Log.e("ERROR LOAD_COINS", t.toString());
                        refreshingListener.stopRefreshing();
                        connection_failed = true;
                    }
                });
    }

    private List<CoinInfo> getCoinInfoList(CoinMarketCup coinMarketCup) {
        List<CoinInfo> coinInfoArrayList = new ArrayList<>();
        List<Datum> coinCoinMarketCupData = coinMarketCup.getData();
        CoinInfo coinInfo;
        for (Datum coin : coinCoinMarketCupData) {
            String fullName = coin.getName();
            String shortName = coin.getSymbol();
            String price = String.valueOf(coin.getQuote().getUSD().getPrice());
            coinInfo = new CoinInfo(fullName, shortName, price);
            coinInfoArrayList.add(coinInfo);
        }
        return coinInfoArrayList;
    }


    public interface OnChangeRefreshingListener {
        void stopRefreshing();
    }

    public void setOnChangeRefreshingListener(OnChangeRefreshingListener refreshingListener) {
        this.refreshingListener = refreshingListener;
    }
}
