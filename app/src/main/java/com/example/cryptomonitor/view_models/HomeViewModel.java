package com.example.cryptomonitor.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private LiveData<List<CoinInfo>> mAllCoinsLiveData;

    public LiveData<List<CoinInfo>> getAllCoinsLiveData() {
        if (mAllCoinsLiveData == null)
            mAllCoinsLiveData = App.getDatabase().coinInfoDao().getAll();
        return mAllCoinsLiveData;
    }
}
