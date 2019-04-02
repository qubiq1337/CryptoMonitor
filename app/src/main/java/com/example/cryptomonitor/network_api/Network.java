package com.example.cryptomonitor.network_api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static Network mInstance;
    private final String BASE_URL = "https://min-api.cryptocompare.com";
    private Retrofit mRetrofit;

    private Network(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Network getmInstance() {
        if (mInstance == null) {
            mInstance = new Network();
        }
        return mInstance;
    }

    public ApiCryptoCompare getApiCryptoCompare(){
        return mRetrofit.create(ApiCryptoCompare.class);
    }
}
