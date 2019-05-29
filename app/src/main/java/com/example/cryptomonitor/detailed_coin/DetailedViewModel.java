package com.example.cryptomonitor.detailed_coin;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.coins.CoinInfo;
import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;

import io.reactivex.disposables.Disposable;

public class DetailedViewModel extends ViewModel {

    private ChartRepo chartRepo = new ChartRepo();
    private Disposable disposable;
    private MutableLiveData<ModelChart> mChartLiveData = new MutableLiveData<>();
    private MutableLiveData<CoinInfo> mCoinLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> mCoinSelectedLiveData = new MutableLiveData<>();

    public LiveData<ModelChart> getChartLiveData() {
        return mChartLiveData;
    }

    public LiveData<Integer> getSelectedCoinLiveData() {
        return mCoinSelectedLiveData;
    }


    public void setChartLiveData(String symbol, String currency, int id) {
        switch (id) {
            case R.id.detailed_1D:
                if (disposable != null) disposable.dispose();
                disposable = chartRepo
                        .getChartData1D(symbol, currency)
                        .subscribe(modelChart -> mChartLiveData.setValue(modelChart),
                                throwable -> Log.e("chartRepo", "Error", throwable)
                        );
                break;

            case R.id.detailed_1W:
                if (disposable != null) disposable.dispose();
                disposable = chartRepo
                        .getChartData1W(symbol, currency)
                        .subscribe(modelChart -> mChartLiveData.setValue(modelChart),
                                throwable -> Log.e("chartRepo", "Error", throwable)
                        );
                break;

            case R.id.detailed_1M:
                if (disposable != null) disposable.dispose();
                disposable = chartRepo
                        .getChartData1M(symbol, currency)
                        .subscribe(modelChart -> mChartLiveData.setValue(modelChart),
                                throwable -> Log.e("chartRepo", "Error", throwable)
                        );
                break;

            case R.id.detailed_3M:
                if (disposable != null) disposable.dispose();
                disposable = chartRepo
                        .getChartData3M(symbol, currency)
                        .subscribe(modelChart -> mChartLiveData.setValue(modelChart),
                                throwable -> Log.e("chartRepo", "Error", throwable)
                        );
                break;
        }
        mCoinSelectedLiveData.setValue(id);
    }

    public void setChartLiveData(String symbol, String currency) {
        mCoinSelectedLiveData.setValue(R.id.detailed_1M);
        disposable = chartRepo
                .getChartData1M(symbol, currency)
                .subscribe(modelChart ->
                                mChartLiveData.setValue(modelChart),
                        throwable -> Log.e("chartRepo", "Error", throwable)
                );
    }

    public LiveData<CoinInfo> getCoinLiveData(String symbol) {
        if (disposable != null) disposable.dispose();
        disposable = chartRepo.getCoinInfo(symbol).subscribe(coinInfo -> mCoinLiveData.setValue(coinInfo));
        return mCoinLiveData;
    }

}
