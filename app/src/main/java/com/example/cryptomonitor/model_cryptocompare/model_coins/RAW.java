
package com.example.cryptomonitor.model_cryptocompare.model_coins;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RAW {

    @SerializedName(value = "USD", alternate = {"EUR","RUB","CNY","GBP"})
    @Expose
    private USD uSD;

    public USD getUSD() {
        return uSD;
    }

    public void setUSD(USD uSD) {
        this.uSD = uSD;
    }

}
