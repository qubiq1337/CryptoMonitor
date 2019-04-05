package com.example.cryptomonitor.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CoinInfoDao {

    @Query("select * from coininfo")
    Flowable<List<CoinInfo>> getAll();

    @Query("select * from coininfo where coinId=:id")
    CoinInfo getById(long id);

    @Query("select * from coininfo where fullName=:fullName")
    List<CoinInfo> getByFullName(String fullName);

    @Insert
    void insert(List<CoinInfo> coinInfoList);

    @Insert
    void insert(CoinInfo coinInfo);

    @Update
    void update(CoinInfo coinInfo);

    @Update
    void update(List<CoinInfo> coinInfoList);

    @Delete
    void delete(CoinInfo coinInfo);

}
