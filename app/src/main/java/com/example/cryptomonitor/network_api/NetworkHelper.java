package com.example.cryptomonitor.network_api;

import android.util.Log;

import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.database.DBHelper;
import com.example.cryptomonitor.model_coinmarket_cup.CoinMarketCup;
import com.example.cryptomonitor.model_coinmarket_cup.Datum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NetworkHelper {

    private final int START_LIMIT = 5000;
    private final int START_PAGE = 1;
    private OnChangeRefreshingListener mRefreshingListener;

    public NetworkHelper(OnChangeRefreshingListener mRefreshingListener) {
        this.mRefreshingListener = mRefreshingListener;
    }

    public void refreshCoins (String currency) {
        loadCoins(currency);
    }

    private void loadCoins(String currency) {
        Network.getInstance()
                .getApiCoinMarketCup()
                .getAllCoinData(START_PAGE, START_LIMIT, currency)
                .enqueue(new Callback<CoinMarketCup>() {
                    @Override
                    public void onResponse(Call<CoinMarketCup> call, Response<CoinMarketCup> response) {
                        if (response.body() != null) {
                            DBHelper.updateDatabase(getCoinInfoList(response.body()));
                        }
                        mRefreshingListener.stopRefreshing(true);
                    }

                    @Override
                    public void onFailure(Call<CoinMarketCup> call, Throwable t) {
                        Log.e("ERROR LOAD_COINS", t.toString());
                        mRefreshingListener.stopRefreshing(false);
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
        void startRefreshing();
        void stopRefreshing(boolean isSuccess);
    }

    public void setOnChangeRefreshingListener(OnChangeRefreshingListener refreshingListener) {
        this.mRefreshingListener = refreshingListener;
    }
}
