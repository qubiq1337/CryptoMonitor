package com.example.cryptomonitor;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CoinInfo {

    @PrimaryKey
    private long coinId;

}
