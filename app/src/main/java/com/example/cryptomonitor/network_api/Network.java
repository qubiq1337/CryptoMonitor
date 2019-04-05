package com.example.cryptomonitor.network_api;

import android.os.Handler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static Network mInstance;
    private Executor mNetworkExecutor;
    private final String BASE_URL = "https://min-api.cryptocompare.com";
    private Retrofit mRetrofit;

    private Network() {
        mNetworkExecutor = Executors.newSingleThreadExecutor();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callbackExecutor(mNetworkExecutor)
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
