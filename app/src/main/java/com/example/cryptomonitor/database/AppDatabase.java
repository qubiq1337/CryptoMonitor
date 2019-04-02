package com.example.cryptomonitor.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {CoinInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CoinInfoDao coinInfoDao();
}
