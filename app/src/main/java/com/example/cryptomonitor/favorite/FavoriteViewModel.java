package com.example.cryptomonitor.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptomonitor.database.coins.CoinDataSource;
import com.example.cryptomonitor.database.coins.CoinRepo;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.events.Event;

import java.util.ArrayList;
import java.util.List;

class FavoriteViewModel extends ViewModel {
    private CoinDataSource mCoinDataSource = new CoinRepo();
    private MutableLiveData<List<CoinInfo>> mFavoriteCoinsLiveData = new MutableLiveData<>();
    private MutableLiveData<Event> mEventMutableLiveData = new MutableLiveData<>();

    FavoriteViewModel() {
        mCoinDataSource.getFavoriteCoins(new CoinDataSource.GetCoinCallback() {
            @Override
            public void onLoaded(List<CoinInfo> coinInfoList) {
                mFavoriteCoinsLiveData.setValue(coinInfoList);
            }

            @Override
            public void onFailed() {
                mFavoriteCoinsLiveData.setValue(new ArrayList<>());
            }
        });
    }

    LiveData<List<CoinInfo>> getFavoriteCoinsLiveData() {
        return mFavoriteCoinsLiveData;
    }

    LiveData<Event> getEventLiveData() {
        return mEventMutableLiveData;
    }

    void onStarClicked(CoinInfo clickedCoinInfo) {
        mCoinDataSource.updateCoin(clickedCoinInfo);
    }
}