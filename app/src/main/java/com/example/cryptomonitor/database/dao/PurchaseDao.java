package com.example.cryptomonitor.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.cryptomonitor.database.entities.Purchase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface PurchaseDao {

    @Query("select * from purchase")
    LiveData<List<Purchase>> getAll();

    @Query("select * from purchase")
    Flowable<List<Purchase>> getAll2();

    @Query("select * from purchase")
    Single<List<Purchase>> getAll3();

    @Insert
    void insert(Purchase purchase);

    @Update
    void update(Purchase purchase);

    @Delete
    void remove(Purchase purchase);

    @Query("delete from purchase where id = :id")
    void removeById(long id);
}
