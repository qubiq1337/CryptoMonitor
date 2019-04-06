package com.example.cryptomonitor.database;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class DBHelper {
    public static void updateDatabase(final List<CoinInfo> newCoinInfoList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CoinInfoDao coinInfoDao = App.getDatabase().coinInfoDao();
                List<CoinInfo> insertList = new ArrayList<>();
                List<CoinInfo> updateList = new ArrayList<>();
                for (CoinInfo coinInfo : newCoinInfoList) {
                    List<CoinInfo> dbInfoList = coinInfoDao.getByFullName(coinInfo.getFullName());
                    if (dbInfoList.isEmpty()) {
                        insertList.add(coinInfo);
                    } else {
                        CoinInfo dbCoinInfo = dbInfoList.get(0);
                        coinInfo.setCoinId(dbCoinInfo.getCoinId());
                        coinInfo.setFavorite(dbCoinInfo.isFavorite());
                        updateList.add(coinInfo);
                    }
                }
                coinInfoDao.insert(insertList);
                coinInfoDao.update(updateList);
            }
        }).start();

    }

    public static void updateCoin(CoinInfo clickedCoinInfo) {
        Observable.fromCallable(new UpdateOperation(clickedCoinInfo))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
