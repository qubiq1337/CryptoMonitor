package com.example.cryptomonitor.database;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBHelper {

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void updateDatabase(final List<CoinInfo> newCoinInfoList) {
        executorService.execute(new Runnable() {
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
        });
    }

    public static void updateCoin(final CoinInfo clickedCoinInfo) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                App.getDatabase().coinInfoDao().update(clickedCoinInfo);
            }
        });
    }

    public static void deleteAllCoins(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                App.getDatabase().coinInfoDao().deleteAll();
            }
        });
    }
}
