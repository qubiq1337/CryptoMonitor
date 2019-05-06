package com.example.cryptomonitor.database;

import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

public interface CoinDataSource {

    interface DataListener {
        void listLoaded(List<CoinInfo> coinInfoList);
    }

    interface RefreshCallback {
        void onSuccess();

        void onFailed();
    }

    void getAllCoins();

    void getFavoriteCoins();

    void refreshCoins(String currency, RefreshCallback refreshCallback);

    void getSearchCoins(String word);

    void updateCoin(CoinInfo coinInfo);

    void updateAll(List<CoinInfo> coinInfoList);
}
