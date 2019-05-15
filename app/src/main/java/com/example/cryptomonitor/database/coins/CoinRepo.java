package com.example.cryptomonitor.database.coins;

import android.util.Log;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.dao.CoinInfoDao;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinCryptoCompare;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinsData;
import com.example.cryptomonitor.network_api.ApiCryptoCompare;
import com.example.cryptomonitor.network_api.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CoinRepo implements CoinDataSource {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final String BASE_IMAGE_URL = "https://www.cryptocompare.com";
    private CoinInfoDao mCoinInfoDao = App.getDatabase().coinInfoDao();
    private ApiCryptoCompare mCoinInfoApi = Network.getInstance().getApiCryptoCompare();
    private Disposable mDisposableSubscription;
    private static Executor dbExecutor = AppExecutors.getInstance().getDbExecutor();

    public Disposable getDisposableSubscription() {
        return mDisposableSubscription;
    }

    @Override
    public void updateAll(List<CoinInfo> coinInfoList) {
        List<CoinInfo> insertList = new ArrayList<>();
        List<CoinInfo> updateList = new ArrayList<>();
        for (CoinInfo coinInfo : coinInfoList) {
            List<CoinInfo> dbInfoList = mCoinInfoDao.getByFullName(coinInfo.getFullName());
            if (dbInfoList.isEmpty()) {
                insertList.add(coinInfo);
            } else {
                CoinInfo dbCoinInfo = dbInfoList.get(0);
                coinInfo.setId(dbCoinInfo.getId());
                coinInfo.setFavorite(dbCoinInfo.isFavorite());
                updateList.add(coinInfo);
            }
        }
        mCoinInfoDao.insert(insertList);
        mCoinInfoDao.update(updateList);
        Log.e("CoinRepo", "updateAll_isLoaded ");
    }

    @Override
    public void updateCoin(CoinInfo coinInfo) {
        dbExecutor.execute(() -> mCoinInfoDao.update(coinInfo));
    }

    @Override
    public void getFavoriteCoins(GetCoinCallback callback) {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();
        mDisposableSubscription = mCoinInfoDao.getFavoriteCoins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded, error -> callback.onFailed());
    }

    @Override
    public void getSearchCoins(String word, GetCoinCallback coinCallback) {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();
        if (word.isEmpty()) {
            coinCallback.onLoaded(new ArrayList<>());
        } else {
            mDisposableSubscription = mCoinInfoDao.getSearchCoins(word)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(coinCallback::onLoaded, error -> coinCallback.onFailed());
        }
    }



    @Override
    public void refreshCoins(String currency, RefreshCallback callback) {
        testRxLoadCoins(currency, callback);
    }

    private void testRxLoadCoins(String currency, RefreshCallback callback) {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(concatObservable(currency)
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .map(CoinCryptoCompare::getData)
                .flatMap(Observable::fromIterable)
                .filter(coinsData -> coinsData.getRAW() != null && coinsData.getDISPLAY() != null)
                .map(this::toCoinInfo)
                .toList()
                .doOnSuccess(this::updateAll)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinInfoList -> {
                            Log.e("testRxLoadCoins", "coinInfoList size:" + coinInfoList.size());
                            callback.onSuccess();
                        },
                        e -> {
                            Log.e("testRxLoadCoins", "FAILED DOWNLOAD: ", e);
                            callback.onFailed();
                        }
                ));

    }

    private Observable<CoinCryptoCompare> concatObservable(String currency) {
        Observable<CoinCryptoCompare> concatObs = mCoinInfoApi.getAllCoins(0, currency);
        for (int i = 0; i < 19; i++)
            concatObs = Observable.concat(concatObs, mCoinInfoApi.getAllCoins(i + 1, currency));
        return concatObs;
    }

    private CoinInfo toCoinInfo(CoinsData coinsData) {
        return new CoinInfo(coinsData.getCoinInfo().getFullName(),
                coinsData.getCoinInfo().getName(),
                coinsData.getRAW().getUSD().getPRICE(), coinsData.getDISPLAY().getUSD().getPRICE(),
                coinsData.getDISPLAY().getUSD().getTOSYMBOL(),
                BASE_IMAGE_URL + coinsData.getCoinInfo().getImageUrl(),
                coinsData.getDISPLAY().getUSD().getCHANGEDAY(),
                coinsData.getRAW().getUSD().getCHANGEDAY(),
                coinsData.getDISPLAY().getUSD().getCHANGEPCTDAY(),
                coinsData.getDISPLAY().getUSD().getSUPPLY(),
                coinsData.getDISPLAY().getUSD().getMKTCAP(),
                coinsData.getDISPLAY().getUSD().getVOLUME24HOUR(),
                coinsData.getDISPLAY().getUSD().getTOTALVOLUME24HTO(),
                coinsData.getDISPLAY().getUSD().getHIGHDAY(),
                coinsData.getDISPLAY().getUSD().getLOWDAY(),
                BASE_IMAGE_URL + coinsData.getCoinInfo().getUrl()
        );
    }
}
