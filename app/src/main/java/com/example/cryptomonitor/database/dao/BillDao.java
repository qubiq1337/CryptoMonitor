package com.example.cryptomonitor.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cryptomonitor.database.entities.Bill;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface BillDao {

    @Query("select * from bill")
    Single<List<Bill>> getAll();

    @Insert
    void insert(Bill bill);

    @Delete
    void delete(Bill bill);

    @Query("delete from bill")
    void clearTable();
}
