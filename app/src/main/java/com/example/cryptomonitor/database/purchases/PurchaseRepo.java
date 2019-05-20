package com.example.cryptomonitor.database.purchases;

import android.util.Log;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.dao.CurrenciesDao;
import com.example.cryptomonitor.database.dao.PurchaseDao;
import com.example.cryptomonitor.database.entities.Purchase;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;

import java.util.List;
import java.util.concurrent.Executor;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PurchaseRepo implements PurchaseDataSource {
    private PurchaseDao mDao;
    private CurrenciesDao mDaoCurrencies;
    private Executor dbExecutor;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public PurchaseRepo() {
        mDao = App.getDatabase().purchaseDao();
        mDaoCurrencies = App.getDatabase().currenciesDao();
        dbExecutor = AppExecutors.getInstance().getDbExecutor();
    }

    @Override
    public void getAllPurchase(GetPurchaseCallback callback) {
        compositeDisposable.add(mDao.getAll1()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded, error -> callback.onFailed()));
    }

    @Override
    public void insert(Purchase purchase) {
        dbExecutor.execute(() -> {
            mDao.insert(purchase);
        });
    }

    @Override
    public void update(Purchase purchase) {
        dbExecutor.execute(() -> {
            mDao.update(purchase);
        });
    }

    @Override
    public void remove(Purchase purchase) {
        dbExecutor.execute(() -> {
            mDao.remove(purchase);
        });
    }

    @Override
    public Flowable<List<PurchaseAndCoin>> getPurchase(long id) {
        return mDao
                .getById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getCurrencyData(GetCurrenciesCallBack getCurrenciesCallBack) {
        compositeDisposable.add(mDaoCurrencies
                .getAllFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getCurrenciesCallBack::onLoaded
                        , throwable -> getCurrenciesCallBack.onFailed())
        );
    }

}
