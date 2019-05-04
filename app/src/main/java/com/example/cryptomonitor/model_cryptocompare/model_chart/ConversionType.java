package com.example.cryptomonitor.model_cryptocompare.model_chart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversionType {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("conversionSymbol")
    @Expose
    private String conversionSymbol;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConversionSymbol() {
        return conversionSymbol;
    }

    public void setConversionSymbol(String conversionSymbol) {
        this.conversionSymbol = conversionSymbol;
    }

}
