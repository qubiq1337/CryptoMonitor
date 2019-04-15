package com.example.cryptomonitor.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<CoinInfo>> mSearchCoinsLiveData;

    public LiveData<List<CoinInfo>> getSearchCoinsLiveData() {
        if (mSearchCoinsLiveData == null)
            mSearchCoinsLiveData = new MutableLiveData<>();
        return mSearchCoinsLiveData;
    }

    public void changeSearchList(final String currentText) {
        if (mSearchCoinsLiveData != null)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<CoinInfo> searchCoins = App.getDatabase().coinInfoDao().getSearchCoins(currentText);
                    mSearchCoinsLiveData.postValue(searchCoins);
                }
            }).start();
    }
}
