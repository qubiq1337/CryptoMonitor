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

    private String priceDisplay;
    private String changeDayDispaly;
    private Double changeDay;
    private String changePctDay;
    private String supply;
    private String mktcap;
    private String volume;
    private String totalVolume24hTo;
    private String high;
    private String low;
    private String infoURL;

    public CoinInfo(String fullName,
                    String shortName,
                    double price,
                    String priceDisplay,
                    String symbol,
                    String imageURL,
                    String changeDayDispaly,
                    Double changeDay,
                    String changePctDay,
                    String supply,
                    String mktcap,
                    String volume,
                    String totalVolume24hTo,
                    String high,
                    String low,
                    String infoURL) {

        this.fullName = fullName;
        this.shortName = shortName;
        this.price = price;
        this.symbol = symbol;
        this.priceDisplay = priceDisplay;
        this.imageURL = imageURL;
        this.isFavorite = false;
        this.changeDayDispaly = changeDayDispaly;
        this.changeDay = changeDay;
        this.changePctDay = changePctDay;
        this.supply = supply;
        this.mktcap = mktcap;
        this.volume = volume;
        this.totalVolume24hTo = totalVolume24hTo;
        this.high = high;
        this.low = low;
        this.infoURL = infoURL;

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

    public String getPriceDisplay() {
        return priceDisplay;
    }

    public void setPriceDisplay(String priceDisplay) {
        this.priceDisplay = priceDisplay;
    }

    public String getChangeDayDispaly() {
        return changeDayDispaly;
    }

    public void setChangeDayDispaly(String changeDayDispaly) {
        this.changeDayDispaly = changeDayDispaly;
    }

    public String getChangePctDay() {
        return changePctDay;
    }

    public void setChangePctDay(String changePctDay) {
        this.changePctDay = changePctDay;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public String getMktcap() {
        return mktcap;
    }

    public void setMktcap(String mktcap) {
        this.mktcap = mktcap;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getTotalVolume24hTo() {
        return totalVolume24hTo;
    }

    public void setTotalVolume24hTo(String totalVolume24hTo) {
        this.totalVolume24hTo = totalVolume24hTo;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getInfoURL() {
        return infoURL;
    }

    public void setInfoURL(String infoURL) {
        this.infoURL = infoURL;
    }

    public Double getChangeDay() {
        return changeDay;
    }

    public void setChangeDay(Double changeDay) {
        this.changeDay = changeDay;
    }
}
