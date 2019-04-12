package com.example.cryptomonitor.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class DBViewModel extends ViewModel {
    private LiveData<List<CoinInfo>> mAllCoinsLiveData;
    private LiveData<List<CoinInfo>> mFavoriteCoinsLiveData;

    public LiveData<List<CoinInfo>> getAllCoinsLiveData() {
        if (mAllCoinsLiveData == null)
            mAllCoinsLiveData = App.getDatabase().coinInfoDao().getAll();
        return mAllCoinsLiveData;
    }

    public LiveData<List<CoinInfo>> getFavoriteCoinsLiveData() {
        if (mFavoriteCoinsLiveData == null)
            mFavoriteCoinsLiveData = App.getDatabase().coinInfoDao().getFavoriteCoins();
        return mFavoriteCoinsLiveData;
    }
}
