package com.example.cryptomonitor.buy;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.PurchaseDataHelper;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.database.entities.Purchase;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.FinishEvent;
import com.example.cryptomonitor.events.Message;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class BuyViewModel extends ViewModel {

    private MutableLiveData<List<CoinInfo>> mSearchLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSelectedCoinVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSearchRecyclerVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSearchEditTextVisible = new MutableLiveData<>();
    private MutableLiveData<Event> mEvent = new MutableLiveData<>();
    private MutableLiveData<String> mDateSet = new MutableLiveData<>();
    private MutableLiveData<CoinInfo> mSelectedCoin = new MutableLiveData<>();
    private CoinInfo mCurrentCoinInfo;
    private Purchase mPurchase;
    private Disposable mDisposableSubscription;
    private Consumer<List<CoinInfo>> mListConsumer = coinInfoList -> mSearchLiveData.postValue(coinInfoList);

    BuyViewModel() {
        defaultSetup();
    }

    void onTextChanged(final String currentText) {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();

        if (!currentText.isEmpty()) {
            mDisposableSubscription = App.getDatabase().coinInfoDao().getSearchCoins(currentText)
                    .subscribeOn(Schedulers.io())
                    .subscribe(mListConsumer);
            if (mCurrentCoinInfo == null) {
                mSearchRecyclerVisible.setValue(true);
            } else {
                mSearchRecyclerVisible.setValue(false);
            }
        } else {
            mSearchRecyclerVisible.setValue(false);
            mSearchLiveData.postValue(new ArrayList<>());
        }
    }

    LiveData<List<CoinInfo>> getSearchLiveData() {
        return mSearchLiveData;
    }

    LiveData<Boolean> getSelectedCoinVisible() {
        return mSelectedCoinVisible;
    }

    LiveData<Boolean> getSearchRecyclerVisible() {
        return mSearchRecyclerVisible;
    }

    LiveData<Boolean> getSearchEditTextVisible() {
        return mSearchEditTextVisible;
    }

    LiveData<String> getDateLiveData() {
        return mDateSet;
    }

    LiveData<CoinInfo> getSelectedCoinLiveData() {
        return mSelectedCoin;
    }

    LiveData<Event> getToastLiveData() {
        return mEvent;
    }

    void onDateSet(int day, int month, int year) {
        mPurchase.setDay(day);
        mPurchase.setMonth(month + 1);
        mPurchase.setYear(year);
        mDateSet.setValue(mPurchase.getDateStr());
    }

    void coinSelected(CoinInfo coinInfo) {
        mCurrentCoinInfo = coinInfo;
        mSearchRecyclerVisible.setValue(false);
        mSearchEditTextVisible.setValue(false);
        mSearchLiveData.setValue(new ArrayList<>());
        mSelectedCoinVisible.setValue(true);
        mSelectedCoin.setValue(coinInfo);
    }

    void coinCancelled() {
        mCurrentCoinInfo = null;
        mSearchEditTextVisible.setValue(true);
        mSelectedCoinVisible.setValue(false);
    }

    void exit() {
        mEvent.setValue(new FinishEvent());
    }

    void ready(String priceStr, String amountStr) {
        double price;
        double amount;
        try {
            price = Double.parseDouble(priceStr);
            amount = Double.parseDouble(amountStr);
            mPurchase.setCoinId(mCurrentCoinInfo.getId());
            mPurchase.setAmount(amount);
            mPurchase.setPrice(price);
            mPurchase.setCoinFullName(mCurrentCoinInfo.getFullName());
            mPurchase.setCoinIndex(mCurrentCoinInfo.getShortName());
            mPurchase.setPriceDisplay(mCurrentCoinInfo.getPriceDisplay());
            PurchaseDataHelper.insert(mPurchase);
            mEvent.setValue(new FinishEvent());
        } catch (NullPointerException e) {
            mEvent.setValue(new Message("Select coin"));
        } catch (NumberFormatException e) {
            mEvent.setValue(new Message("Wrong format"));
        }
    }

    private void defaultSetup() {
        mPurchase = new Purchase();
        mCurrentCoinInfo = null;
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();
        mSearchLiveData.setValue(new ArrayList<>());
        mSelectedCoin.setValue(mCurrentCoinInfo);
        mDateSet.setValue("");
        mSearchRecyclerVisible.setValue(false);
        mSearchEditTextVisible.setValue(true);
        mSelectedCoinVisible.setValue(false);
    }
}
