package com.example.cryptomonitor.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CoinInfoDao {

    @Query("select * from coininfo")
    Flowable<List<CoinInfo>> getAll();

    @Query("select * from coininfo where coinId=:id")
    CoinInfo getById(long id);

    @Query("select * from coininfo where fullName=:fullName")
    List<CoinInfo> getByFullName(String fullName);

    @Query("select * from coininfo where isFavorite = 1 ")
    Flowable<List<CoinInfo>> getFavoriteCoins();

    @Insert
    void insert(List<CoinInfo> coinInfoList);

    @Insert
    void insert(CoinInfo coinInfo);

    @Update
    int update(CoinInfo coinInfo);

    @Update
    int update(List<CoinInfo> coinInfoList);

    @Delete
    int delete(CoinInfo coinInfo);
}
