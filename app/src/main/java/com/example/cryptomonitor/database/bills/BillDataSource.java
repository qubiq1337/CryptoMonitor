package com.example.cryptomonitor.database.bills;

import java.util.List;

public interface BillDataSource {
    void getAll(GetBillsCallback callback);

    void insert(Bill bill);

    void delete(Bill bill);

    void deleteAll();

    interface GetBillsCallback {
        void onLoaded(List<Bill> billList);

        void onFailed();
    }
}
