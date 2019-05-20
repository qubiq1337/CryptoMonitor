package com.example.cryptomonitor.model_cryptocompare.model_currencies;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class CurrenciesData {

    @PrimaryKey(autoGenerate = true)
    private long id_currency;
    @SerializedName("USD")
    @Expose
    private Double mUSD;
    @SerializedName("EUR")
    @Expose
    private Double mEUR;
    @SerializedName("RUB")
    @Expose
    private Double mRUB;
    @SerializedName("CNY")
    @Expose
    private Double mCNY;
    @SerializedName("GBP")
    @Expose
    private Double mGBP;

    public Double getUSD() {
        return mUSD;
    }

    public void setUSD(Double uSD) {
        this.mUSD = uSD;
    }

    public Double getEUR() {
        return mEUR;
    }

    public void setEUR(Double eUR) {
        this.mEUR = eUR;
    }

    public Double getRUB() {
        return mRUB;
    }

    public void setRUB(Double rUB) {
        this.mRUB = rUB;
    }

    public Double getCNY() {
        return mCNY;
    }

    public void setCNY(Double cNY) {
        this.mCNY = cNY;
    }

    public Double getGBP() {
        return mGBP;
    }

    public void setGBP(Double gBP) {
        this.mGBP = gBP;
    }

    public long getId_currency() {
        return id_currency;
    }

    public void setId_currency(long id_currency) {
        this.id_currency = id_currency;
    }


}
