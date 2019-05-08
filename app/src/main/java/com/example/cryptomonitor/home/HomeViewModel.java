package com.example.cryptomonitor.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cryptomonitor.database.coins.CoinDataSource;
import com.example.cryptomonitor.database.coins.CoinRepo;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.Message;

import java.util.ArrayList;
import java.util.List;

class HomeViewModel extends ViewModel {

    private MutableLiveData<List<CoinInfo>> mSearchModeLiveData = new MutableLiveData<>();
    private MutableLiveData<Event> mEventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsRefreshLiveData = new MutableLiveData<>();
    private CoinDataSource mCoinRepository = new CoinRepo();

    LiveData<List<CoinInfo>> getSearchModeLiveData() {
        return mSearchModeLiveData;
    }

    LiveData<Event> getEventLiveData() {
        return mEventMutableLiveData;
    }

    LiveData<Boolean> getSwipeRefreshLiveData() {
        return mIsRefreshLiveData;
    }

    void onTextChanged(final String currentText) {
        mCoinRepository.getSearchCoins(currentText, new CoinDataSource.GetCoinCallback() {
            @Override
            public void onLoaded(List<CoinInfo> coinInfoList) {
                mSearchModeLiveData.setValue(coinInfoList);
            }

            @Override
            public void onFailed() {
                mEventMutableLiveData.setValue(new Message("Failed"));
            }
        });
    }

    void onStarClicked(CoinInfo clickedCoinInfo) {
        mCoinRepository.updateCoin(clickedCoinInfo);
    }

    void onSearchClicked() {
        mSearchModeLiveData.setValue(new ArrayList<>());
    }

    void onSearchDeactivated() {
        mSearchModeLiveData.setValue(null);
    }

    void refresh(String currency) {
        mIsRefreshLiveData.setValue(true);
        mCoinRepository.refreshCoins(currency, new CoinDataSource.RefreshCallback() {
            @Override
            public void onSuccess() {
                mEventMutableLiveData.setValue(new Message("Successful"));
                mIsRefreshLiveData.setValue(false);
            }

            @Override
            public void onFailed() {
                mEventMutableLiveData.setValue(new Message("Failed"));
                mIsRefreshLiveData.setValue(false);
            }
        });
    }
}
