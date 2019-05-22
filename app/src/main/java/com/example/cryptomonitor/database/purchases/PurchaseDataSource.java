package com.example.cryptomonitor.database.purchases;

import com.example.cryptomonitor.database.entities.Purchase;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface PurchaseDataSource {

    interface GetPurchaseCallback {
        void onLoaded(List<PurchaseAndCoin> purchaseList);

        void onFailed();
    }
    interface GetCurrenciesCallBack {
        void onLoaded(CurrenciesData currenciesData);

        void onFailed();
    }

    void getAllPurchase(GetPurchaseCallback callback);

    void insert(Purchase purchase);

    void update(Purchase purchase);

    void remove(Purchase purchase);

    Single<List<PurchaseAndCoin>> getPurchase(long id);

    void getCurrencyData(GetCurrenciesCallBack getCurrenciesCallBack);
}
