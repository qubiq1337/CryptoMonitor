package com.example.cryptomonitor.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.cryptomonitor.database.bills.Bill;
import com.example.cryptomonitor.database.bills.BillDao;
import com.example.cryptomonitor.database.coins.CoinInfo;
import com.example.cryptomonitor.database.coins.CoinInfoDao;
import com.example.cryptomonitor.database.dao.CurrenciesDao;
import com.example.cryptomonitor.database.purchases.Purchase;
import com.example.cryptomonitor.database.purchases.PurchaseDao;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;

@Database(entities = {CoinInfo.class, Purchase.class, CurrenciesData.class, Bill.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CoinInfoDao coinInfoDao();

    public abstract PurchaseDao purchaseDao();

    public abstract BillDao billDao();

    public abstract CurrenciesDao currenciesDao();
}
