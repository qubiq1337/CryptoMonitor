package com.example.cryptomonitor.database;

import java.util.List;
import java.util.concurrent.Callable;

public class UpdateOperation implements Callable<Integer> {

    private List<CoinInfo> coinInfoList;
    private CoinInfo coinInfo;

    public UpdateOperation(CoinInfo coinInfo) {
        this.coinInfo = coinInfo;
    }

    public UpdateOperation(List<CoinInfo> coinInfoList) {
        this.coinInfoList = coinInfoList;
    }

    @Override
    public Integer call() {
        if (coinInfoList != null)
            return App.getDatabase().coinInfoDao().update(coinInfoList);
        else if (coinInfo != null)
            return App.getDatabase().coinInfoDao().update(coinInfo);
        return -1;
    }
}
