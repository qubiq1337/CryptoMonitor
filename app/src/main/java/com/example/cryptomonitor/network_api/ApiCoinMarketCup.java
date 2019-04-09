package com.example.cryptomonitor.network_api;

import com.example.cryptomonitor.model_coinmarket_cup.CoinMarketCup;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiCoinMarketCup {


    @Headers({"Accept: application/json","X-CMC_PRO_API_KEY: a1127750-ac8b-4a27-858c-12f6211fd4f0"})

    @GET("/v1/cryptocurrency/listings/latest?sort=market_cap")
    Call<CoinMarketCup> getAllCoinData(@Query("start") int start,@Query("limit") int limit,@Query("convert") String convert);

}
