
package com.example.cryptomonitor.model_cryptocompare.model_chart;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelChart {

    @SerializedName("Response")
    @Expose
    private String response;
    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("Aggregated")
    @Expose
    private Boolean aggregated;
    @SerializedName("Data")
    @Expose
    private List<ChartData> data = null;
    @SerializedName("TimeTo")
    @Expose
    private Double timeTo;
    @SerializedName("TimeFrom")
    @Expose
    private Double timeFrom;
    @SerializedName("FirstValueInArray")
    @Expose
    private Boolean firstValueInArray;
    @SerializedName("ConversionType")
    @Expose
    private ConversionType conversionType;
    @SerializedName("RateLimit")
    @Expose
    private RateLimit rateLimit;
    @SerializedName("HasWarning")
    @Expose
    private Boolean hasWarning;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getAggregated() {
        return aggregated;
    }

    public void setAggregated(Boolean aggregated) {
        this.aggregated = aggregated;
    }

    public List<ChartData> getData() {
        return data;
    }

    public void setData(List<ChartData> data) {
        this.data = data;
    }

    public Double getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Double timeTo) {
        this.timeTo = timeTo;
    }

    public Double getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Double timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Boolean getFirstValueInArray() {
        return firstValueInArray;
    }

    public void setFirstValueInArray(Boolean firstValueInArray) {
        this.firstValueInArray = firstValueInArray;
    }

    public ConversionType getConversionType() {
        return conversionType;
    }

    public void setConversionType(ConversionType conversionType) {
        this.conversionType = conversionType;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    public Boolean getHasWarning() {
        return hasWarning;
    }

    public void setHasWarning(Boolean hasWarning) {
        this.hasWarning = hasWarning;
    }

}
