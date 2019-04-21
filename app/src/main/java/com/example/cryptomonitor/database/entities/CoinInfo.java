package com.example.cryptomonitor.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.math.RoundingMode;
import java.text.DecimalFormat;

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

    public CoinInfo(String fullName, String shortName, double price, String symbol) {
        this.fullName = fullName;
        this.shortName = shortName;
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

    public String getPriceStr(){
        DecimalFormat format = new DecimalFormat("#.##");
        format.setRoundingMode(RoundingMode.CEILING);
        return format.format(this.price).concat(this.symbol);
    }
}
