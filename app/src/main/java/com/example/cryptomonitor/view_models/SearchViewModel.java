package com.example.cryptomonitor.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;

public class SearchViewModel extends ViewModel {

    private LiveData<PagedList<CoinInfo>> mSearchLiveData;

    public LiveData<PagedList<CoinInfo>> getSearchLiveData(String currentText) {
        if (mSearchLiveData == null) {
            DataSource.Factory<Integer, CoinInfo> sourceFactory = App.getDatabase().coinInfoDao().getSearchCoins(currentText);
            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPageSize(10)
                    .build();
            mSearchLiveData = new LivePagedListBuilder<>(sourceFactory, config)
                    .setFetchExecutor(AppExecutors.getInstance().getDbExecutor())
                    .build();
        }
        return mSearchLiveData;
    }

    public LiveData<PagedList<CoinInfo>> getSearchLiveData() {
        if (mSearchLiveData == null)
            return getSearchLiveData("");
        else return mSearchLiveData;

    }
}
