package com.example.cryptomonitor.database.coins;

import java.util.List;

public interface CoinDataSource {

    void getFavoriteCoins(GetCoinCallback coinCallback);

    void refreshCoins(String currency, RefreshCallback refreshCallback);

    void getSearchCoins(String word, GetCoinCallback coinCallback);

    void getSearchFavoriteCoins(String word, GetCoinCallback coinCallback);

    void updateCoin(CoinInfo coinInfo);

    void updateAll(List<CoinInfo> coinInfoList);

    interface RefreshCallback {
        void onSuccess();

        void onFailed();
    }

    interface GetCoinCallback {
        void onLoaded(List<CoinInfo> coinInfoList);

        void onFailed();
    }

}
