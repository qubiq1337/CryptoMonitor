package com.example.cryptomonitor.network_api;


import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinCryptoCompare;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCryptoCompare {

    @GET("/data/histohour?")
    Observable<ModelChart> getChartDataHours(@Query("fsym") String symbol,
                                             @Query("tsym") String currency,
                                             @Query("aggregate") int aggregate,
                                             @Query("limit") int limit);
    @GET("/data/histominute?")
    Observable<ModelChart> getChartDataMinutes(@Query("fsym") String symbol,
                                               @Query("tsym") String currency,
                                               @Query("aggregate") int aggregate,
                                               @Query("limit") int limit);

    @GET("/data/top/mktcapfull?limit=100")
    Observable<CoinCryptoCompare> getAllCoins(@Query("page") int page, @Query("tsym") String currency);


}