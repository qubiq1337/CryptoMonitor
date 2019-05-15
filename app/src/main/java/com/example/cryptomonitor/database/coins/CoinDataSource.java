package com.example.cryptomonitor.database.coins;

import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

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

    Disposable getDisposableSubscription();


}
