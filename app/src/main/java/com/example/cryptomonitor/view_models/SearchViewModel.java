package com.example.cryptomonitor.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private LiveData<List<CoinInfo>> mSearchViewLiveData;

    public LiveData<List<CoinInfo>> getNewSearchLiveData(String currentText) {
        mSearchViewLiveData = App.getDatabase().coinInfoDao().getSearchCoins(currentText);
        return mSearchViewLiveData;
    }

    public LiveData<List<CoinInfo>> getCurrentSearchLiveData() {
        if (mSearchViewLiveData == null)
            return getNewSearchLiveData("");
        else
            return mSearchViewLiveData;
    }
}
