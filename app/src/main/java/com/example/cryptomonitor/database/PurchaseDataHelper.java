package com.example.cryptomonitor.database;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.entities.Purchase;

import java.util.concurrent.Executor;

public class PurchaseDataHelper {
    private static Executor executor = AppExecutors.getInstance().getDbExecutor();

    public static void insert(final Purchase purchase) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                App.getDatabase().purchaseDao().insert(purchase);
            }
        });
    }
}
