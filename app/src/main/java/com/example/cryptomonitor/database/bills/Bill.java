package com.example.cryptomonitor.database.bills;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bill {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String full_name;
    private String short_name;
    private String buy_date;
    private String sell_date;
    private String buy_currency_symbol;
    private double amount;
    private double buyPrice;
    private double sellPrice;
    private String image_url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getBuy_date() {
        return buy_date;
    }

    public void setBuy_date(String buy_date) {
        this.buy_date = buy_date;
    }

    public String getSell_date() {
        return sell_date;
    }

    public void setSell_date(String sell_date) {
        this.sell_date = sell_date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBuy_currency_symbol() {
        return buy_currency_symbol;
    }

    public void setBuy_currency_symbol(String buy_currency_symbol) {
        this.buy_currency_symbol = buy_currency_symbol;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}


