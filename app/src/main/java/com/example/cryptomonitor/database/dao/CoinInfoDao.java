package com.example.cryptomonitor.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.widget.SmallCoin;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CoinInfoDao {

    @Query("select * from coininfo limit :last")
    Flowable<List<CoinInfo>> getAllBefore(int last);

    @Query("select * from coininfo where id=:id")
    CoinInfo getById(long id);

    @Query("select fullName, shortName, imageURL, priceDisplay, symbol from coininfo where isFavorite = 1")
    Single<List<SmallCoin>> getWidgetList();

    @Query("select * from coininfo where shortName=:shortName")
    Flowable<List<CoinInfo>> getByShortName(String shortName);

    @Query("select * from coininfo ")
    Flowable<List<CoinInfo>> getAll();

    @Query("select * from coininfo where fullName=:fullName")
    List<CoinInfo> getByFullName(String fullName);

    @Query("select * from coininfo where isFavorite = 1 order by fullName")
    Flowable<List<CoinInfo>> getFavoriteCoins();

    @Query("select * from coininfo where fullName like :search || '%' order by fullName")
    Flowable<List<CoinInfo>> getSearchCoins(String search);

    @Query("select * from coininfo where fullName like :search || '%' and isFavorite = 1 order by fullName")
    Flowable<List<CoinInfo>> getSearchFavoriteCoins(String search);

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
