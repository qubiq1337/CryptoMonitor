package com.example.cryptomonitor.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.coins.CoinDataSource;
import com.example.cryptomonitor.database.coins.CoinInfo;
import com.example.cryptomonitor.database.coins.CoinRepo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.Message;

import java.util.ArrayList;
import java.util.List;

class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<CoinInfo>> mSearchModeLiveData = new MutableLiveData<>();
    private MutableLiveData<Event> mEventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsRefreshLiveData = new MutableLiveData<>();
    private CoinDataSource mCoinRepository = new CoinRepo();
    private boolean isRefreshing = false;

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

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
                mEventMutableLiveData.setValue(new Message(getString(R.string.updating_failed)));
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
        if (!isRefreshing) {
            isRefreshing = true;
            mIsRefreshLiveData.setValue(true);
            mCoinRepository.refreshCoins(currency, new CoinDataSource.RefreshCallback() {
                @Override
                public void onSuccess() {
                    mEventMutableLiveData.setValue(new Message(getString(R.string.update_success)));
                    isRefreshing = false;
                    mIsRefreshLiveData.setValue(false);
                }

                @Override
                public void onFailed() {
                    mEventMutableLiveData.setValue(new Message(getString(R.string.updating_failed)));
                    isRefreshing = false;
                    mIsRefreshLiveData.setValue(false);
                }
            });
        }
    }

    private String getString(int resId) {
        return getApplication().getString(resId);
    }
}