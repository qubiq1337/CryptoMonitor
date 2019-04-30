
package com.example.cryptomonitor.model_cryptocompare.model_coins;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DISPLAY {

    @SerializedName(value = "USD", alternate = {"EUR","RUB","CNY","GBP"})
    @Expose
    private USD_ uSD;

    public USD_ getUSD() {
        return uSD;
    }

    public void setUSD(USD_ uSD) {
        this.uSD = uSD;
    }

}
