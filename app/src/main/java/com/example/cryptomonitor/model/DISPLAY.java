
package com.example.cryptomonitor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DISPLAY {

    @SerializedName("USD")
    @Expose
    private USD_ uSD;

    public USD_ getUSD() {
        return uSD;
    }

    public void setUSD(USD_ uSD) {
        this.uSD = uSD;
    }

}
