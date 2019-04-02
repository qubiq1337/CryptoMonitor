
package com.example.cryptomonitor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("CoinInfo")
    @Expose
    private CoinInfo coinInfo;
    @SerializedName("RAW")
    @Expose
    private RAW rAW;
    @SerializedName("DISPLAY")
    @Expose
    private DISPLAY dISPLAY;

    public CoinInfo getCoinInfo() {
        return coinInfo;
    }

    public void setCoinInfo(CoinInfo coinInfo) {
        this.coinInfo = coinInfo;
    }

    public RAW getRAW() {
        return rAW;
    }

    public void setRAW(RAW rAW) {
        this.rAW = rAW;
    }

    public DISPLAY getDISPLAY() {
        return dISPLAY;
    }

    public void setDISPLAY(DISPLAY dISPLAY) {
        this.dISPLAY = dISPLAY;
    }

}
