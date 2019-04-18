package com.example.cryptomonitor.database;

import android.util.Log;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.dao.CoinInfoDao;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


public class CoinDataHelper {

    private static Executor dbExecutor = AppExecutors.getInstance().getDbExecutor();

    public static void updateDatabase(final List<CoinInfo> newCoinInfoList) {
        dbExecutor.execute(new Runnable() {
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
                        coinInfo.setId(dbCoinInfo.getId());
                        coinInfo.setFavorite(dbCoinInfo.isFavorite());
                        updateList.add(coinInfo);
                    }
                }
                coinInfoDao.insert(insertList);
                coinInfoDao.update(updateList);
                Log.e("DbHelper", "isLoaded ");
            }
        });
    }

    public static void updateCoin(final CoinInfo clickedCoinInfo) {
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                App.getDatabase().coinInfoDao().update(clickedCoinInfo);
            }
        });
    }

    public static void deleteAllCoins() {
        dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                App.getDatabase().coinInfoDao().deleteAll();
            }
        });
    }
}

