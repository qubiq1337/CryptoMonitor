package com.example.cryptomonitor;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;

import io.reactivex.disposables.Disposable;

public class DeteiledViewModel extends ViewModel {

    private ChartRepo chartRepo = new ChartRepo();
    private Disposable disposable;
    private MutableLiveData<ModelChart> mChartLiveData = new MutableLiveData<>();
    private MutableLiveData<CoinInfo> mCoinLiveData = new MutableLiveData<>();

    public LiveData<ModelChart> getChartLiveData() {
        return mChartLiveData;
    }


    public void setChartLiveData(String symbol, String currency, int id) {
        switch (id) {
            case R.id.detailed_1D:
                if (disposable != null) disposable.dispose();
                disposable = chartRepo
                        .getChartData1D(symbol, currency)
                        .subscribe(modelChart -> mChartLiveData.setValue(modelChart));
                break;

            case R.id.detailed_1W:
                if (disposable != null) disposable.dispose();
                disposable = chartRepo
                        .getChartData1W(symbol, currency)
                        .subscribe(modelChart -> mChartLiveData.setValue(modelChart));
                break;

            case R.id.detailed_1M:
                if (disposable != null) disposable.dispose();
                disposable = chartRepo
                        .getChartData1M(symbol, currency)
                        .subscribe(modelChart -> mChartLiveData.setValue(modelChart));
                break;

            case R.id.detailed_3M:
                if (disposable != null) disposable.dispose();
                disposable = chartRepo
                        .getChartData3M(symbol, currency)
                        .subscribe(modelChart -> mChartLiveData.setValue(modelChart));
                break;
        }
    }

    public void setChartLiveData(String symbol, String currency) {
        disposable = chartRepo
                .getChartData1M(symbol, currency)
                .subscribe(modelChart ->
                                mChartLiveData.setValue(modelChart),
                        throwable -> Log.e("chartRepo", "Eror", throwable)
                );
    }

    public LiveData<CoinInfo> getCoinLiveData(String symbol) {
        if (disposable != null) disposable.dispose();
        disposable = chartRepo.getCoinInfo(symbol).subscribe(coinInfo -> mCoinLiveData.setValue(coinInfo));
        return mCoinLiveData;
    }

}
