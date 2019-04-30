package com.example.cryptomonitor.network_api;





import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinCryptoCompare;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCryptoCompare {

    @GET("/data/histohour?aggregate=6&limit=120")
    Observable<ModelChart> getChartData(@Query ("fsym") String symbol,@Query ("tsym") String currency);

    @GET("/data/top/mktcapfull?limit=100")
    Observable<CoinCryptoCompare> getAllCoins(@Query("page")int page , @Query ("tsym") String currency);


}