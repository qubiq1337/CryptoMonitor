package com.example.cryptomonitor.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private LiveData<List<CoinInfo>> mFavoriteCoinsLiveData;

    public LiveData<List<CoinInfo>> getFavoriteCoinsLiveData() {
        if (mFavoriteCoinsLiveData == null)
            mFavoriteCoinsLiveData = App.getDatabase().coinInfoDao().getFavoriteCoins();
        return mFavoriteCoinsLiveData;
    }
}
