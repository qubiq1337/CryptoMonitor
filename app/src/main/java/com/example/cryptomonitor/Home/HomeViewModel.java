package com.example.cryptomonitor.Home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.CoinDataHelper;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class HomeViewModel extends ViewModel {

    private final static int initialSize = 60;
    private final static int loadSize = 20;
    private MutableLiveData<List<CoinInfo>> mCoinListLiveData = new MutableLiveData<>();
    private int lastIndex;
    private boolean mIsSearchActive = false;
    private Disposable mDisposableSubscription;
    private Consumer<List<CoinInfo>> mListConsumer = new Consumer<List<CoinInfo>>() {
        @Override
        public void accept(@Nullable List<CoinInfo> coinInfoList) {
            mCoinListLiveData.postValue(coinInfoList);
            lastIndex = coinInfoList.size();
        }
    };

    HomeViewModel() {
        setStartList();
    }

    LiveData<List<CoinInfo>> getCoinsLiveData() {
        return mCoinListLiveData;
    }


    void onEndReached() {
        if (!mIsSearchActive) {
            mDisposableSubscription.dispose();
            mDisposableSubscription = App
                    .getDatabase()
                    .coinInfoDao()
                    .getAllBefore(lastIndex + loadSize)
                    .subscribeOn(Schedulers.io())
                    .subscribe(mListConsumer);
        }
    }

    void onTextChanged(final String currentText) {
        if (currentText.isEmpty()) {
            mDisposableSubscription.dispose();
            sendEmptyList();
        } else {
            if (mDisposableSubscription != null)
                mDisposableSubscription.dispose();

            mDisposableSubscription = App
                    .getDatabase()
                    .coinInfoDao()
                    .getSearchCoins(currentText)
                    .subscribeOn(Schedulers.io())
                    .subscribe(mListConsumer);
        }
    }

    void onStarClicked(CoinInfo clickedCoinInfo) {
        if (clickedCoinInfo.isFavorite())
            clickedCoinInfo.setFavorite(false);
        else
            clickedCoinInfo.setFavorite(true);
        CoinDataHelper.updateCoin(clickedCoinInfo);
    }

    void onSearchClicked() {
        mIsSearchActive = true;
        mDisposableSubscription.dispose();
        sendEmptyList();
    }

    void onSearchDeactivated() {
        mIsSearchActive = false;
        setStartList();
    }

    private void setStartList() {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();

        mDisposableSubscription = App
                .getDatabase()
                .coinInfoDao()
                .getAllBefore(initialSize)
                .subscribeOn(Schedulers.io())
                .subscribe(mListConsumer);
    }

    private void sendEmptyList() {
        mCoinListLiveData.postValue(new ArrayList<CoinInfo>());
    }
}


