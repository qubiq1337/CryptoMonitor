
package com.example.cryptomonitor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RAW {

    @SerializedName("USD")
    @Expose
    private USD uSD;

    public USD getUSD() {
        return uSD;
    }

    public void setUSD(USD uSD) {
        this.uSD = uSD;
    }

}
