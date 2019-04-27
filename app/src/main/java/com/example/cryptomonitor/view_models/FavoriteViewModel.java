package com.example.cryptomonitor.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<List<CoinInfo>> mFavoriteCoinsLiveData = new MutableLiveData<>();

    public LiveData<List<CoinInfo>> getFavoriteCoinsLiveData() {
        final Disposable subscribe = App.getDatabase().coinInfoDao().getFavoriteCoins()
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<CoinInfo>>() {
                    @Override
                    public void accept(List<CoinInfo> coinInfoList) {
                        mFavoriteCoinsLiveData.postValue(coinInfoList);
                    }
                });
        return mFavoriteCoinsLiveData;
    }
}
