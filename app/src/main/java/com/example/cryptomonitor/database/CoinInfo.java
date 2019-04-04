package com.example.cryptomonitor.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CoinInfo {

    @PrimaryKey
    private long coinId;
    private String fullName;
    private String shortName;
    private String imageURL;
    private String price;

    public CoinInfo(String fullName, String shortName, String imageURL, String price) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.imageURL = imageURL;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getCoinId() {
        return coinId;
    }

    public void setCoinId(long coinId) {
        this.coinId = coinId;
    }
}
