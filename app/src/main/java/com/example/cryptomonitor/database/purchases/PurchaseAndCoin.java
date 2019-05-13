package com.example.cryptomonitor.database.purchases;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;

import com.example.cryptomonitor.database.entities.Purchase;

public class PurchaseAndCoin {

    @Embedded
    private Purchase mPurchase;

    @ColumnInfo(name = "coin_price_display")
    private String priceDisplay;

    @ColumnInfo(name = "coin_full_name")
    private String coinFullName;

    @ColumnInfo(name = "coin_short_name")
    private String coinIndex;

    @ColumnInfo(name = "coin_url")
    private String iconURL;

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
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

    public Purchase getPurchase() {
        return mPurchase;
    }

    public void setPurchase(Purchase purchase) {
        mPurchase = purchase;
    }
}
