package com.example.cryptomonitor.events;

public class PriceEvent extends Event {
    private String mSymbol;
    private String mPrice;

    public PriceEvent(String symbol, String price) {
        mSymbol = symbol;
        mPrice = price;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public String getPrice() {
        return mPrice;
    }
}
