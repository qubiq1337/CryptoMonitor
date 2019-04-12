package com.example.cryptomonitor.network_api;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Network {
    private static Network mInstance;
    private final String BASE_URL = "https://pro-api.coinmarketcap.com";
    private Retrofit mRetrofit;

    private Network() {
        Executor networkExecutor = Executors.newSingleThreadExecutor();
        mRetrofit = new Retrofit.Builder()
                .callbackExecutor(networkExecutor)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    static Network getInstance() {
        if (mInstance == null) {
            mInstance = new Network();
        }
        return mInstance;
    }

    ApiCoinMarketCup getApiCoinMarketCup() {
        return mRetrofit.create(ApiCoinMarketCup.class);
    }
}
