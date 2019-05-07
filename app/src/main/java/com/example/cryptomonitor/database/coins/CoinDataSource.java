package com.example.cryptomonitor.database.coins;

import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

public interface CoinDataSource {

    interface RefreshCallback {
        void onSuccess();

        void onFailed();
    }

    interface GetCoinCallback {
        void onLoaded(List<CoinInfo> coinInfoList);

        void onFailed();
    }

    void getFavoriteCoins(GetCoinCallback coinCallback);

    void refreshCoins(String currency, RefreshCallback refreshCallback);

    void getSearchCoins(String word, GetCoinCallback coinCallback);

    void updateCoin(CoinInfo coinInfo);

    void updateAll(List<CoinInfo> coinInfoList);
}
