package com.example.cryptomonitor.model_coinmarket_cup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote {

    @SerializedName(value = "USD", alternate = {"EUR", "RUB", "CNY", "GBP"})
    @Expose
    private USD uSD;

    public USD getUSD() {
        return uSD;
    }

    public void setUSD(USD uSD) {
        this.uSD = uSD;
    }

}
