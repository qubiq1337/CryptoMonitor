package com.example.cryptomonitor;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.dao.CoinInfoDao;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;
import com.example.cryptomonitor.network_api.ApiCryptoCompare;
import com.example.cryptomonitor.network_api.Network;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChartRepo {

    private ApiCryptoCompare apiCryptoCompare = Network.getInstance().getApiCryptoCompare();
    private CoinInfoDao coinInfoDao = App.getDatabase().coinInfoDao();

    private Observable<ModelChart> getChartData(String symbol, String currency, int aggregate, int limit) {
        return apiCryptoCompare
                .getChartDataHours(symbol, currency, aggregate, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ModelChart> getChartData1D(String symbol, String currency) {
        return apiCryptoCompare
                .getChartDataMinutes(symbol, currency, 12, 119)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ModelChart> getChartData1W(String symbol, String currency) {
        return getChartData(symbol, currency, 1, 168);
    }

    public Observable<ModelChart> getChartData1M(String symbol, String currency) {
        return getChartData(symbol, currency, 6, 120);
    }

    public Observable<ModelChart> getChartData3M(String symbol, String currency) {
        return getChartData(symbol, currency, 18, 120);
    }

    public Flowable<CoinInfo> getCoinInfo(String symbol) {
        return coinInfoDao
                .getByShortName(symbol)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(coinInfoList -> !coinInfoList.isEmpty())
                .flatMap(Flowable::fromIterable);
    }

}
