
package com.example.cryptomonitor.model_coinmarket_cup;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("circulating_supply")
    @Expose
    private Double circulatingSupply;
    @SerializedName("total_supply")
    @Expose
    private Double totalSupply;
    @SerializedName("max_supply")
    @Expose
    private Double maxSupply;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("num_market_pairs")
    @Expose
    private Integer numMarketPairs;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("platform")
    @Expose
    private Object platform;
    @SerializedName("cmc_rank")
    @Expose
    private Integer cmcRank;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;
    @SerializedName("quote")
    @Expose
    private Quote quote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Double getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(Double circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public Double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(Double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Double getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(Double maxSupply) {
        this.maxSupply = maxSupply;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getNumMarketPairs() {
        return numMarketPairs;
    }

    public void setNumMarketPairs(Integer numMarketPairs) {
        this.numMarketPairs = numMarketPairs;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Object getPlatform() {
        return platform;
    }

    public void setPlatform(Object platform) {
        this.platform = platform;
    }

    public Integer getCmcRank() {
        return cmcRank;
    }

    public void setCmcRank(Integer cmcRank) {
        this.cmcRank = cmcRank;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

}
