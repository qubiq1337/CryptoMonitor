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

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<CoinInfo>> mSearchLiveData = new MutableLiveData<>();
    private Disposable mDisposableSubscription;
    private Consumer<List<CoinInfo>> mListConsumer = new Consumer<List<CoinInfo>>() {
        @Override
        public void accept(@Nullable List<CoinInfo> coinInfoList) {
            mSearchLiveData.postValue(coinInfoList);
        }
    };

    public void onTextChanged(final String currentText) {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();
        mDisposableSubscription = App.getDatabase().coinInfoDao().getSearchCoins(currentText)
                .subscribeOn(Schedulers.io())
                .subscribe(mListConsumer);
    }

    public LiveData<List<CoinInfo>> getSearchLiveData() {
        return mSearchLiveData;
    }
}
