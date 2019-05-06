package com.example.cryptomonitor.database;

import android.util.Log;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.database.dao.CoinInfoDao;
import com.example.cryptomonitor.database.entities.CoinInfo;
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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CoinRepo implements CoinDataSource {

    private int lastIndex;
    private final static int initialSize = 60;
    private final static int loadSize = 20;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final String BASE_IMAGE_URL = "https://www.cryptocompare.com";
    private CoinInfoDao mCoinInfoDao = App.getDatabase().coinInfoDao();
    private ApiCryptoCompare mCoinInfoApi = Network.getInstance().getApiCryptoCompare();
    private Disposable mDisposableSubscription;
    private DataListener mDataListener;
    private static Executor dbExecutor = AppExecutors.getInstance().getDbExecutor();
    private Consumer<List<CoinInfo>> mListConsumer = new Consumer<List<CoinInfo>>() {
        @Override
        public void accept(List<CoinInfo> coinInfoList) {
            mDataListener.listLoaded(coinInfoList);
            lastIndex = coinInfoList.size();
        }
    };

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

    public CoinRepo(DataListener dataListener) {
        mDataListener = dataListener;
        setStartList();
    }

    @Override
    public void updateCoin(CoinInfo coinInfo) {
        dbExecutor.execute(() -> App.getDatabase().coinInfoDao().update(coinInfo));
    }

    @Override
    public void getAllCoins() {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();
        mDisposableSubscription = App.getDatabase().coinInfoDao().getAllBefore(initialSize)
                .subscribeOn(Schedulers.io())
                .subscribe(mListConsumer);
    }

    @Override
    public void getFavoriteCoins() {

    }

    @Override
    public void getSearchCoins(String word) {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();
        if (word.isEmpty()) {
            mDataListener.listLoaded(new ArrayList<>());
        } else {
            mDisposableSubscription = App.getDatabase().coinInfoDao().getSearchCoins(word)
                    .subscribeOn(Schedulers.io())
                    .subscribe(mListConsumer);
        }
    }

    @Override
    public void refreshCoins(String currency, RefreshCallback callback) {
        testRxLoadCoins(currency, callback);
    }

    public void loadMore() {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();
        mDisposableSubscription = App.getDatabase().coinInfoDao().getAllBefore(lastIndex + loadSize)
                .subscribeOn(Schedulers.io())
                .subscribe(mListConsumer);
    }

    private void setStartList() {
        if (mDisposableSubscription != null)
            mDisposableSubscription.dispose();
        mDisposableSubscription = App.getDatabase().coinInfoDao().getAllBefore(initialSize)
                .subscribeOn(Schedulers.io())
                .subscribe(mListConsumer);
    }

    private void testRxLoadCoins(String currency, RefreshCallback callback) {
        compositeDisposable.add(concatObservable(currency)
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .observeOn(AndroidSchedulers.mainThread())
                .map(CoinCryptoCompare::getData)
                .flatMap(Observable::fromIterable)
                .filter(coinsData -> coinsData.getRAW() != null && coinsData.getDISPLAY() != null)
                .map(this::toCoinInfo)
                .toList()
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

    }

    private Observable<CoinCryptoCompare> concatObservable(String currency) {
        Observable<CoinCryptoCompare> concatObs = Network.getInstance().getApiCryptoCompare().getAllCoins(0, currency);
        for (int i = 0; i < 19; i++)
            concatObs = Observable.concat(concatObs, Network.getInstance().getApiCryptoCompare().getAllCoins(i + 1, currency));
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
