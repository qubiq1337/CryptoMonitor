package com.example.cryptomonitor.buy;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptomonitor.database.coins.CoinDataSource;
import com.example.cryptomonitor.database.coins.CoinRepo;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.database.entities.Purchase;
import com.example.cryptomonitor.database.purchases.PurchaseDataSource;
import com.example.cryptomonitor.database.purchases.PurchaseRepo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.FinishEvent;
import com.example.cryptomonitor.events.Message;
import com.example.cryptomonitor.events.PriceEvent;

import java.util.ArrayList;
import java.util.List;

class BuyViewModel extends ViewModel {

    private MutableLiveData<List<CoinInfo>> mSearchLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSelectedCoinVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSearchRecyclerVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSearchEditTextVisible = new MutableLiveData<>();
    private MutableLiveData<Event> mPriceEventLiveData = new MutableLiveData<>();
    private MutableLiveData<Event> mEvent = new MutableLiveData<>();
    private MutableLiveData<String> mDateSet = new MutableLiveData<>();
    private MutableLiveData<CoinInfo> mSelectedCoin = new MutableLiveData<>();
    private CoinInfo mCurrentCoinInfo;
    private Purchase mPurchase;
    private CoinDataSource mCoinDataSource = new CoinRepo();
    private PurchaseDataSource mPurchaseDataSource = new PurchaseRepo();

    BuyViewModel() {
        defaultSetup();
    }

    void onTextChanged(final String currentText) {
        if (!currentText.isEmpty()) {
            mCoinDataSource.getSearchCoins(currentText, new CoinDataSource.GetCoinCallback() {
                @Override
                public void onLoaded(List<CoinInfo> coinInfoList) {
                    mSearchLiveData.setValue(coinInfoList);
                }

                @Override
                public void onFailed() {
                    mSearchLiveData.setValue(new ArrayList<>());
                }
            });
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

    LiveData<Event> getPriceLiveData() {
        return mPriceEventLiveData;
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
        String price = String.valueOf(coinInfo.getPrice());
        String symbol = coinInfo.getSymbol();
        mPriceEventLiveData.setValue(new PriceEvent(symbol, price));
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
        if (priceStr.isEmpty() || amountStr.isEmpty()) {
            mEvent.setValue(new Message("Fill empty fields"));
            Log.e("Ready", (priceStr));
        } else
            try {
                mPurchase.setCoinId(mCurrentCoinInfo.getId());
                price = Double.parseDouble(priceStr);
                amount = Double.parseDouble(amountStr);
                mPurchase.setAmount(amount);
                mPurchase.setPrice_purchase(price);
                mPurchaseDataSource.insert(mPurchase);
                Log.e("Ready", (mPurchase.getPrice_purchase() + " " + mPurchase.getAmount() + mPurchase.getDateStr()));
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
        mSearchLiveData.setValue(new ArrayList<>());
        mSelectedCoin.setValue(mCurrentCoinInfo);
        mDateSet.setValue("");
        mSearchRecyclerVisible.setValue(false);
        mSearchEditTextVisible.setValue(true);
        mSelectedCoinVisible.setValue(false);
    }
}
