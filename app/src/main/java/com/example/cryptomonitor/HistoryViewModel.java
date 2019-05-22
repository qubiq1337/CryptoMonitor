package com.example.cryptomonitor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptomonitor.database.bills.BillDataSource;
import com.example.cryptomonitor.database.bills.BillRepo;
import com.example.cryptomonitor.database.entities.Bill;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    private MutableLiveData<List<Bill>> billsLiveData = new MutableLiveData<>();
    private BillDataSource billDataSource = new BillRepo();

    public HistoryViewModel() {
        billDataSource.getAll(new BillDataSource.GetBillsCallback() {
            @Override
            public void onLoaded(List<Bill> billList) {
                billsLiveData.setValue(billList);
            }

            @Override
            public void onFailed() {

            }
        });
    }

    public LiveData<List<Bill>> getBillsLiveData() {
        return billsLiveData;
    }
}
