package com.example.cryptomonitor.database.bills;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.App;

import java.util.concurrent.Executor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BillRepo implements BillDataSource {

    private static Executor dbExecutor = AppExecutors.getInstance().getDbExecutor();
    private BillDao mDao = App.getDatabase().billDao();

    @Override
    public void getAll(GetBillsCallback callback) {
        Disposable subscribe = mDao.getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded, err -> callback.onFailed());
    }

    @Override
    public void insert(Bill bill) {
        dbExecutor.execute(() -> mDao.insert(bill));
    }

    @Override
    public void delete(Bill bill) {
        dbExecutor.execute(() -> mDao.delete(bill));
    }

    @Override
    public void deleteAll() {
        dbExecutor.execute(() -> mDao.clearTable());
    }
}
