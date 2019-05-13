package com.example.cryptomonitor.database.purchases;

import com.example.cryptomonitor.database.entities.Purchase;

import java.util.List;

public interface PurchaseDataSource {

    interface GetPurchaseCallback {
        void onLoaded(List<PurchaseAndCoin> purchaseList);

        void onFailed();
    }

    void getAllPurchase(GetPurchaseCallback callback);

    void insert(Purchase purchase);
}
