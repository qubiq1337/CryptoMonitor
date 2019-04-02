package com.example.cryptomonitor.network_api;

import com.example.cryptomonitor.model.CoinCryptoCompare;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCryptoCompare {
    @GET ("/data/top/totalvolfull")
    Call<CoinCryptoCompare> getTopListData(@Query("limit") int limit, @Query("page") int page, @Query("tsym") String cash );

    @GET ("/data/top/totalvolfull?limit=10&page=0&tsym=USD")
    Call<CoinCryptoCompare> getTopListDataTEST();
}
