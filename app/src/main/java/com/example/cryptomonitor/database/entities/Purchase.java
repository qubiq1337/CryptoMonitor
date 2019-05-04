package com.example.cryptomonitor.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = CoinInfo.class, parentColumns = "id", childColumns = "coinId"))
public class Purchase {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long coinId;
    private int day;
    private int month;
    private int year;
    private double price;
    private String USD;
    private double amount;

    private String priceDisplay;
    private String coinFullName;
    private String coinIndex;


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUSD() {
        return USD;
    }

    public void setUSD(String USD) {
        this.USD = USD;
    }

    public String getDateStr() {
        return month < 10 ?
                this.getDay() + ".0" + this.getMonth() + "." + this.getYear()
                : this.getDay() + "." + this.getMonth() + "." + this.getYear();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCoinId() {
        return coinId;
    }

    public void setCoinId(long coinId) {
        this.coinId = coinId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPriceDisplay() {
        return priceDisplay;
    }

    public void setPriceDisplay(String priceDisplay) {
        this.priceDisplay = priceDisplay;
    }

    public String getCoinFullName() {
        return coinFullName;
    }

    public void setCoinFullName(String coinFullName) {
        this.coinFullName = coinFullName;
    }

    public String getCoinIndex() {
        return coinIndex;
    }

    public void setCoinIndex(String coinIndex) {
        this.coinIndex = coinIndex;
    }
}
