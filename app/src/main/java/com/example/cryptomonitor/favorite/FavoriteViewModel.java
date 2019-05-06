package com.example.cryptomonitor.favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.CoinDataHelper;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class FavoriteViewModel extends ViewModel {
    FavoriteViewModel() {
        final Disposable subscribe = App.getDatabase().coinInfoDao().getFavoriteCoins()
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<CoinInfo>>() {
                    @Override
                    public void accept(List<CoinInfo> coinInfoList) {
                        mFavoriteCoinsLiveData.postValue(coinInfoList);
                    }
                });
    }

    private MutableLiveData<List<CoinInfo>> mFavoriteCoinsLiveData = new MutableLiveData<>();

    LiveData<List<CoinInfo>> getFavoriteCoinsLiveData() {
        return mFavoriteCoinsLiveData;
    }

    void onStarClicked(CoinInfo clickedCoinInfo) {
        if (clickedCoinInfo.isFavorite())
            clickedCoinInfo.setFavorite(false);
        else
            clickedCoinInfo.setFavorite(true);
        CoinDataHelper.updateCoin(clickedCoinInfo);
    }
}
