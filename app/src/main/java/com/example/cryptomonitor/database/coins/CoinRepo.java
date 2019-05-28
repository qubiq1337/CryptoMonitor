package com.example.cryptomonitor.database.coins;

import android.util.Log;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinCryptoCompare;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinsData;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;
import com.example.cryptomonitor.network_api.ApiCryptoCompare;
import com.example.cryptomonitor.network_api.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class CoinRepo implements CoinDataSource {

    private static Executor dbExecutor = AppExecutors.getInstance().getDbExecutor();
    private final String BASE_IMAGE_URL = "https://www.cryptocompare.com";
    private CoinInfoDao mCoinInfoDao = App.getDatabase().coinInfoDao();
    private ApiCryptoCompare mCoinInfoApi = Network.getInstance().getApiCryptoCompare();
    private Disposable mDisposableSubscription;

    @Override
    public void getSearchFavoriteCoins(String word, GetCoinCallback coinCallback) {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();
        if (word.isEmpty()) {
            coinCallback.onLoaded(new ArrayList<>());
        } else {
            mDisposableSubscription = mCoinInfoDao.getSearchFavoriteCoins(word)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(coinCallback::onLoaded, error -> coinCallback.onFailed());
        }
    }

    @Override
    public void updateAll(List<CoinInfo> coinInfoList) {
        dbExecutor.execute(() -> {
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
            Log.e("DbHelper", "isLoaded ");
        });

    }

    @Override
    public void updateCoin(CoinInfo oldCoin) {
        dbExecutor.execute(() -> {
            CoinInfo newCoin = mCoinInfoDao.getById(oldCoin.getId());
            newCoin.setFavorite(oldCoin.isFavorite());
            mCoinInfoDao.update(newCoin);
        });
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

       /* compositeDisposable.add(concatObservable(currency)
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .map(CoinCryptoCompare::getData)
                .flatMap(Observable::fromIterable)
                .filter(coinsData -> coinsData.getRAW() != null && coinsData.getDISPLAY() != null)
                .map(this::toCoinInfo)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinInfoList -> {
                            Log.e("testRxLoadCoins", "coinInfoList size:" + coinInfoList.size());
                            callback.onSuccess();
                            updateAll(coinInfoList);
                        },
                        e -> {
                            Log.e("testRxLoadCoins", "FAILED DOWNLOAD: ", e);
                            callback.onFailed();
                        }
                ));

        compositeDisposable.add(mCoinInfoApi
                .getAllCurrencies(currency)
                .subscribeOn(Schedulers.io())
                .subscribe(this::updateCurrencies, e -> Log.e("aDDADDSDADAD", "testRxLoadCoins: ", e))
        );*/

        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();

        Single<List<CoinInfo>> test1 = mergeObservable(currency)
                .map(CoinCryptoCompare::getData)
                .flatMap(Observable::fromIterable)
                .filter(coinsData -> coinsData.getRAW() != null && coinsData.getDISPLAY() != null)
                .map(this::toCoinInfo)
                .toList();

        Single<CurrenciesData> test2 = mCoinInfoApi.getAllCurrencies(currency);


        mDisposableSubscription = Observable
                .zip(test1.toObservable(), test2.toObservable(), new BiFunction<List<CoinInfo>, CurrenciesData, List<CoinInfo>>() {
                    @Override
                    public List<CoinInfo> apply(List<CoinInfo> coinInfoList, CurrenciesData currenciesData) throws Exception {
                        updateAll(coinInfoList);
                        updateCurrencies(currenciesData);
                        return coinInfoList;
                    }
                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinInfoList -> {
                            Log.e("testRxLoadCoins", "coinInfoList size:" + coinInfoList.size());
                            callback.onSuccess();
                        }, e -> {
                            Log.e("testRxLoadCoins", "FAILED DOWNLOAD: ", e);
                            callback.onFailed();
                        }
                );
    }

    private Observable<CoinCryptoCompare> mergeObservable(String currency) {
        Observable<CoinCryptoCompare> mergeObs = mCoinInfoApi.getAllCoins(0, currency);
        for (int i = 0; i < 19; i++)
            mergeObs = Observable.mergeDelayError(mergeObs, mCoinInfoApi.getAllCoins(i + 1, currency));
        return mergeObs;
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
                BASE_IMAGE_URL + coinsData.getCoinInfo().getUrl(),
                coinsData.getRAW().getUSD().getMKTCAP()
        );
    }


    private void updateCurrencies(CurrenciesData currenciesData) {
        CurrenciesData dbCurrencies = App.getDatabase().currenciesDao().getAll();
        if (dbCurrencies == null) {
            App.getDatabase().currenciesDao().insert(currenciesData);
            Log.e("updateCurrencies", "insert " + currenciesData.getRUB());
        } else {
            currenciesData.setId_currency(dbCurrencies.getId_currency());
            App.getDatabase().currenciesDao().update(currenciesData);
            Log.e("updateCurrencies", "update " + currenciesData.getRUB());
        }
    }

}
