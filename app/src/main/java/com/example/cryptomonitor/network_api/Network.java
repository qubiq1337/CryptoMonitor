package com.example.cryptomonitor.network_api;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static Network mInstance;
    private final String BASE_URL = "https://min-api.cryptocompare.com";
    private Executor mNetworkExecutor;
    private Retrofit mRetrofit;

    private Network() {
        mNetworkExecutor = Executors.newSingleThreadExecutor();
        mRetrofit = new Retrofit.Builder()
                .callbackExecutor(mNetworkExecutor)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Network getInstance() {
        if (mInstance == null) {
            mInstance = new Network();
        }
        return mInstance;
    }

    public ApiCryptoCompare getApiCryptoCompare() {
        return mRetrofit.create(ApiCryptoCompare.class);
    }
}
