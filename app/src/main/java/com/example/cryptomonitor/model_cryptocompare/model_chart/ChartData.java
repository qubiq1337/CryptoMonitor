package com.example.cryptomonitor.model_cryptocompare.model_chart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChartData {

    @SerializedName("time")
    @Expose
    private Double time;
    @SerializedName("close")
    @Expose
    private Double close;
    @SerializedName("high")
    @Expose
    private Double high;
    @SerializedName("low")
    @Expose
    private Double low;
    @SerializedName("open")
    @Expose
    private Double open;
    @SerializedName("volumefrom")
    @Expose
    private Double volumefrom;
    @SerializedName("volumeto")
    @Expose
    private Double volumeto;

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getVolumefrom() {
        return volumefrom;
    }

    public void setVolumefrom(Double volumefrom) {
        this.volumefrom = volumefrom;
    }

    public Double getVolumeto() {
        return volumeto;
    }

    public void setVolumeto(Double volumeto) {
        this.volumeto = volumeto;
    }

}
