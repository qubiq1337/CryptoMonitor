package com.example.cryptomonitor.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<CoinInfo>> mListMutableLiveData = new MutableLiveData<>();
    private int lastIndex;
    private final static int initialSize = 60;
    private final static int loadSize = 20;
    private Disposable mDisposableSubscription;
    private Consumer<List<CoinInfo>> mListConsumer = new Consumer<List<CoinInfo>>() {
        @Override
        public void accept(@Nullable List<CoinInfo> coinInfoList) {
            mListMutableLiveData.postValue(coinInfoList);
            lastIndex = coinInfoList.size();
        }
    };

    public LiveData<List<CoinInfo>> getAllCoinsLiveData() {
        mDisposableSubscription = App.getDatabase().coinInfoDao().getAllBefore(initialSize)
                .subscribeOn(Schedulers.io())
                .subscribe(mListConsumer);
        return mListMutableLiveData;
    }

    public void onEndReached() {
        mDisposableSubscription.dispose();
        mDisposableSubscription = App.getDatabase().coinInfoDao().getAllBefore(lastIndex + loadSize)
                .subscribeOn(Schedulers.io())
                .subscribe(mListConsumer);
    }
}
