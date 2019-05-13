package com.example.cryptomonitor.database.purchases;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.dao.PurchaseDao;
import com.example.cryptomonitor.database.entities.Purchase;

import java.util.concurrent.Executor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PurchaseRepo implements PurchaseDataSource {
    private PurchaseDao mDao;
    private Executor dbExecutor;

    public PurchaseRepo() {
        mDao = App.getDatabase().purchaseDao();
        dbExecutor = AppExecutors.getInstance().getDbExecutor();
    }

    @Override
    public void getAllPurchase(GetPurchaseCallback callback) {
        Disposable subscribe = mDao.getAll1()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded, error -> callback.onFailed());
    }

    @Override
    public void insert(Purchase purchase) {
        dbExecutor.execute(() -> mDao.insert(purchase));
    }
}
