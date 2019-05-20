package com.example.cryptomonitor.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cryptomonitor.database.entities.Purchase;
import com.example.cryptomonitor.database.purchases.PurchaseAndCoin;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PurchaseDao {

    @Query("select * from purchase")
    Flowable<List<Purchase>> getAll();

    @Query("select *, coinInfo.priceDisplay as coin_price_display" +
            ", coinInfo.fullName as coin_full_name" +
            ", coinInfo.shortName as coin_short_name" +
            ", coinInfo.imageURL as coin_url" +
            ", coinInfo.price as coin_price" +
            " from purchase, coinInfo where purchase.coinId == coinInfo.id")
    Flowable<List<PurchaseAndCoin>> getAll1();

    @Insert
    void insert(Purchase purchase);

    @Query("select *, coinInfo.priceDisplay as coin_price_display" +
            ", coinInfo.fullName as coin_full_name" +
            ", coinInfo.shortName as coin_short_name" +
            ", coinInfo.imageURL as coin_url" +
            ", coinInfo.price as coin_price" +
            " from purchase, coinInfo where purchase.coinId == coinInfo.id and purchase.purchase_id = :id")
    Flowable<List<PurchaseAndCoin>> getById(long id);

    @Update
    void update(Purchase purchase);

    @Delete
    void remove(Purchase purchase);

    @Query("delete from purchase where purchase_id = :id")
    void removeById(long id);
}
