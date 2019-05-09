package com.example.cryptomonitor.database.purchases;

import com.example.cryptomonitor.database.entities.Purchase;

public interface PurchaseDataSource {
    void insert(Purchase purchase);
}
