package com.example.cryptomonitor.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.fragment.HomeFragment;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<CoinInfo>> mSearchCoinsLiveData;

    public LiveData<List<CoinInfo>> getSearchCoinsLiveData() {
        if (mSearchCoinsLiveData == null)
            mSearchCoinsLiveData = new MutableLiveData<>();
        return mSearchCoinsLiveData;
    }

    public void changeSearchList(final String currentText){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CoinInfo> searchCoins = App.getDatabase().coinInfoDao().getSearchCoins(currentText);
                Log.d(HomeFragment.TAG, "run: " + searchCoins.size());
                mSearchCoinsLiveData.postValue(searchCoins);
            }
        }).start();
    }
}
