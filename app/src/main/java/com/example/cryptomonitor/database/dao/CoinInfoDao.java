package com.example.cryptomonitor.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.List;

@Dao
public interface CoinInfoDao {

    @Query("select * from coininfo order by fullName")
    LiveData<List<CoinInfo>> getAll();

    @Query("select * from coininfo where id=:id")
    CoinInfo getById(long id);

    @Query("select * from coininfo where fullName=:fullName")
    List<CoinInfo> getByFullName(String fullName);

    @Query("select * from coininfo where isFavorite = 1 order by fullName")
    LiveData<List<CoinInfo>> getFavoriteCoins();

    @Query("select * from coininfo where fullName like :search || '%' order by fullName")
    LiveData<List<CoinInfo>> getSearchCoins(String search);

    @Query("select count(*) from coininfo")
    int getDatabaseSize();

    @Query("delete from coininfo")
    void deleteAll();

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
