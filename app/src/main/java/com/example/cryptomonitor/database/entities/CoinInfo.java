package com.example.cryptomonitor.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

@Entity
public class CoinInfo {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String fullName;
    private String shortName;
    private String imageURL;
    private double price;
    private String symbol;
    private boolean isFavorite;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CoinInfo coinInfo = (CoinInfo) o;
        return Double.compare(coinInfo.price, price) == 0 &&
                isFavorite == coinInfo.isFavorite &&
                Objects.equals(fullName, coinInfo.fullName) &&
                Objects.equals(shortName, coinInfo.shortName) &&
                Objects.equals(imageURL, coinInfo.imageURL) &&
                Objects.equals(symbol, coinInfo.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, shortName, imageURL, price, symbol, isFavorite);
    }

    public CoinInfo(String fullName, String shortName, String imageURL, double price, String symbol) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.imageURL = imageURL;
        this.price = price;
        this.symbol = symbol;
        this.isFavorite = false;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPriceStr() {
        DecimalFormat format = new DecimalFormat("#.##");
        format.setRoundingMode(RoundingMode.CEILING);
        return format.format(this.price).concat(this.symbol);
    }
}
