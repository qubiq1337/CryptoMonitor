package com.example.cryptomonitor.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;

public class HomeViewModel extends ViewModel {
    private LiveData<PagedList<CoinInfo>> mAllCoinsLiveData;

    public LiveData<PagedList<CoinInfo>> getAllCoinsLiveData() {
        if (mAllCoinsLiveData == null) {
            DataSource.Factory<Integer, CoinInfo> sourceFactory = App.getDatabase().coinInfoDao().getAll();
            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPageSize(20)
                    .build();
            mAllCoinsLiveData = new LivePagedListBuilder<>(sourceFactory, config)
                    .setFetchExecutor(AppExecutors.getInstance().getDbExecutor())
                    .build();
        }
        return mAllCoinsLiveData;
    }
}
