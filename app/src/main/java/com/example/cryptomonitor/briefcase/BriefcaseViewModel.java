package com.example.cryptomonitor.briefcase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptomonitor.database.purchases.Purchase;
import com.example.cryptomonitor.database.purchases.PurchaseAndCoin;
import com.example.cryptomonitor.database.purchases.PurchaseDataSource;
import com.example.cryptomonitor.database.purchases.PurchaseRepo;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;
import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BriefcaseViewModel extends ViewModel {
    private MutableLiveData<List<PurchaseAndCoin>> mPurchaseAndCoinLive = new MutableLiveData<>();
    private MutableLiveData<List<PieEntry>> mPieLiveData = new MutableLiveData<>();
    private PurchaseDataSource mPurchaseRepo = new PurchaseRepo();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<CurrenciesData> currenciesLiveData = new MutableLiveData<>();

    public BriefcaseViewModel() {
        mPurchaseRepo.getAllPurchase(new PurchaseDataSource.GetPurchaseCallback() {
            @Override
            public void onLoaded(List<PurchaseAndCoin> purchaseList) {
                mPurchaseAndCoinLive.setValue(purchaseList);
                compositeDisposable.add(Observable.fromIterable(purchaseList)
                        .groupBy(PurchaseAndCoin::getCoinFullName)
                        .flatMapSingle(group ->
                                group.map(purchaseAndCoin -> purchaseAndCoin.getPurchase().getAmount() * purchaseAndCoin.getPurchase().getPrice_purchase())
                                        .reduce(0d, (Double x, Double y) -> x + y)
                                        .map(aDouble -> new PieEntry(aDouble.floatValue(), group.getKey() + ""))
                        )
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(pieEntries -> mPieLiveData.setValue(pieEntries)));
            }

            @Override
            public void onFailed() {
                //ignored
            }
        });

        mPurchaseRepo.getCurrencyData(new PurchaseDataSource.GetCurrenciesCallBack() {
            @Override
            public void onLoaded(CurrenciesData currenciesData) {
                currenciesLiveData.setValue(currenciesData);
            }

            @Override
            public void onFailed() {
                //ignored
            }
        });
    }

    LiveData<List<PurchaseAndCoin>> getPurchaseAndCoinLive() {
        return mPurchaseAndCoinLive;
    }

    LiveData<List<PieEntry>> getPieLiveData() {
        return mPieLiveData;
    }

    LiveData<CurrenciesData> getCurrenciesLiveData() {
        return currenciesLiveData;
    }

    void removeSwipedItem(Purchase purchase) {
        mPurchaseRepo.remove(purchase);
    }

}
