package com.example.cryptomonitor.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;

import io.reactivex.Flowable;

@Dao
public interface CurrenciesDao {

    @Query("select * from currenciesdata ")
    CurrenciesData getAll();

    @Query("select * from currenciesdata ")
    Flowable<CurrenciesData> getAllFlowable();

    @Update
    void update(CurrenciesData currenciesData);

    @Insert
    void insert(CurrenciesData currenciesData);

}
