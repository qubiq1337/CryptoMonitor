package com.example.cryptomonitor.network_api;


import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinCryptoCompare;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCryptoCompare {

    @GET("/data/histohour?aggregate=6&limit=120")
    Observable<ModelChart> getChartData(@Query("fsym") String symbol, @Query("tsym") String currency);

    @GET("/data/top/mktcapfull?limit=100")
    Observable<CoinCryptoCompare> getAllCoins(@Query("page") int page, @Query("tsym") String currency);

    @GET ("/data/price?&tsyms=USD,EUR,RUB,CNY,GBP")
    Single<CurrenciesData> getAllCurrencies (@Query("fsym") String currency);

}