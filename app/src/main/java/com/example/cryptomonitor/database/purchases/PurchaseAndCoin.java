package com.example.cryptomonitor.database.purchases;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class PurchaseAndCoin {

    @Embedded
    private Purchase mPurchase;

    @ColumnInfo(name = "coin_price_display")
    private String priceDisplay;

    @ColumnInfo(name = "coin_full_name")
    private String coinFullName;

    @ColumnInfo(name = "coin_price")
    private Double double_price;

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

    public Double getDouble_price() {
        return double_price;
    }

    public void setDouble_price(Double double_price) {
        this.double_price = double_price;
    }
}
