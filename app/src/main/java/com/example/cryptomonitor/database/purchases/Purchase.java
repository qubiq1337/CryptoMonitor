package com.example.cryptomonitor.database.purchases;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.cryptomonitor.database.coins.CoinInfo;

@Entity(foreignKeys = @ForeignKey(entity = CoinInfo.class, parentColumns = "id", childColumns = "coinId"))
public class Purchase {
    @PrimaryKey(autoGenerate = true)
    private long purchase_id;
    private long coinId;
    private int day;
    private int month;
    private int year;
    private double price_purchase;
    private String USD;
    private String buyCurrencySymbol;
    private String buyCurrency;
    private double amount;


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice_purchase() {
        return price_purchase;
    }

    public void setPrice_purchase(double price_purchase) {
        this.price_purchase = price_purchase;
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

    public long getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(long purchase_id) {
        this.purchase_id = purchase_id;
    }

    public String getBuyCurrencySymbol() {
        return buyCurrencySymbol;
    }

    public void setBuyCurrencySymbol(String buyCurrencySymbol) {
        this.buyCurrencySymbol = buyCurrencySymbol;
    }

    public String getBuyCurrency() {
        return buyCurrency;
    }

    public void setBuyCurrency(String buyCurrency) {
        this.buyCurrency = buyCurrency;
    }
}
