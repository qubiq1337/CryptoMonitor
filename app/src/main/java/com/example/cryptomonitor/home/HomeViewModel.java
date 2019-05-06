package com.example.cryptomonitor.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cryptomonitor.database.CoinDataSource;
import com.example.cryptomonitor.database.CoinRepo;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.Message;

import java.util.List;

class HomeViewModel extends ViewModel implements CoinDataSource.DataListener {

    private MutableLiveData<List<CoinInfo>> mCoinListLiveData = new MutableLiveData<>();
    private MutableLiveData<Event> mEventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsRefreshLiveData = new MutableLiveData<>();
    private boolean mIsSearchActive = false;
    private CoinRepo mCoinRepository = new CoinRepo(this);

    HomeViewModel() {
        mCoinRepository.getAllCoins();
    }

    LiveData<List<CoinInfo>> getCoinsLiveData() {
        return mCoinListLiveData;
    }

    LiveData<Event> getEventLiveData() {
        return mEventMutableLiveData;
    }

    LiveData<Boolean> getSwipeRefreshLiveData() {
        return mIsRefreshLiveData;
    }

    @Override
    public void listLoaded(List<CoinInfo> coinInfoList) {
        mCoinListLiveData.postValue(coinInfoList);
    }

    void onEndReached() {
        if (!mIsSearchActive) {
            mCoinRepository.loadMore();
        }
    }

    void onTextChanged(final String currentText) {
        mCoinRepository.getSearchCoins(currentText);
    }

    void onStarClicked(CoinInfo clickedCoinInfo) {
        if (clickedCoinInfo.isFavorite())
            clickedCoinInfo.setFavorite(false);
        else
            clickedCoinInfo.setFavorite(true);
        mCoinRepository.updateCoin(clickedCoinInfo);
    }

    void onSearchClicked() {
        mIsSearchActive = true;
        mCoinRepository.getSearchCoins("");
    }

    void onSearchDeactivated() {
        mIsSearchActive = false;
        mCoinRepository.getAllCoins();
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
