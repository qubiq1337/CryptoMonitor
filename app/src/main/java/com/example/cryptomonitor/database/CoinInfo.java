package com.example.cryptomonitor.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CoinInfo {

    @PrimaryKey
    private long coinId;
    private String fullName;
    private String shortName;
    private String wayToIcon;

    String getFullName() {
        return fullName;
    }

    void setFullName(String fullName) {
        this.fullName = fullName;
    }

    String getShortName() {
        return shortName;
    }

    void setShortName(String shortName) {
        this.shortName = shortName;
    }

    String getWayToIcon() {
        return wayToIcon;
    }

    void setWayToIcon(String wayToIcon) {
        this.wayToIcon = wayToIcon;
    }

    long getCoinId() {
        return coinId;
    }

    void setCoinId(long coinId) {
        this.coinId = coinId;
    }
}
