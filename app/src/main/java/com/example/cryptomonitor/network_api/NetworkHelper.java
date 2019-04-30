package com.example.cryptomonitor.network_api;

import android.util.Log;

import com.example.cryptomonitor.database.CoinDataHelper;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinCryptoCompare;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinsData;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class NetworkHelper {

   /* private final int START_LIMIT = 5000;
    private final int START_PAGE = 1;*/
    private OnChangeRefreshingListener mRefreshingListener;
    private static HashMap<String, String> coinSymbols = new HashMap<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final String BASE_IMAGE_URL = "https://www.cryptocompare.com";
    static {
        coinSymbols.put("USD", "\u0024");
        coinSymbols.put("EUR", "\u20AC");
        coinSymbols.put("RUB", "\u20BD");
        coinSymbols.put("CNY", "\u5713");
        coinSymbols.put("GBP", "\uFFE1");
    }

    public NetworkHelper(OnChangeRefreshingListener mRefreshingListener) {
        this.mRefreshingListener = mRefreshingListener;
    }

    public void refreshCoins(String currency) {
        testRxLoadCoins(currency);
    }

   /* private void loadCoins(final String currency) {
        mRefreshingListener.startRefreshing();
        Network.getInstance()
                .getApiCryptoCompare()
                .getAllCoinData(START_PAGE, START_LIMIT, currency)
                .enqueue(new Callback<CoinMarketCup>() {
                    @Override
                    public void onResponse(@NonNull Call<CoinMarketCup> call, @NonNull Response<CoinMarketCup> response) {
                        if (response.body() != null) {
                            CoinDataHelper.updateDatabase(getCoinInfoList(response.body(), currency));
                        }
                        mRefreshingListener.stopRefreshing(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<CoinMarketCup> call, @NonNull Throwable t) {
                        Log.e("ERROR LOAD_COINS", t.toString());
                        mRefreshingListener.stopRefreshing(false);
                    }
                });
    }

    private List<CoinInfo> getCoinInfoList(CoinMarketCup coinMarketCup, String currency) {
        List<CoinInfo> coinInfoArrayList = new ArrayList<>();
        List<ChartData> coinCoinMarketCupData = coinMarketCup.getData();
        CoinInfo coinInfo;

        for (ChartData coin : coinCoinMarketCupData) {
            String fullName = coin.getName();
            String shortName = coin.getSymbol();
            double price = coin.getQuote().getUSD().getPrice();
            String symbol = coinSymbols.get(currency);
            coinInfo = new CoinInfo(fullName, shortName, price, symbol);
            coinInfoArrayList.add(coinInfo);
        }
        return coinInfoArrayList;
    }*/


    public interface OnChangeRefreshingListener {
        void startRefreshing();

        void stopRefreshing(boolean isSuccess);
    }

    private void testRxLoadCoins(String currency) {
        mRefreshingListener.startRefreshing();

        compositeDisposable.add(concatObservable(currency)
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .observeOn(AndroidSchedulers.mainThread())
                .map(CoinCryptoCompare::getData)
                .flatMap(Observable::fromIterable)
                .filter(coinsData -> coinsData.getRAW() != null && coinsData.getDISPLAY() != null)
                .map(this::toCoinInfo)
                .toList()
                .subscribe(coinInfoList -> {
                    Log.e("testRxLoadCoins", "coinInfoList size:"+ String.valueOf(coinInfoList.size()));
                    CoinDataHelper.updateDatabase(coinInfoList);
                    mRefreshingListener.stopRefreshing(true);
                })
        );

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
                BASE_IMAGE_URL+coinsData.getCoinInfo().getImageUrl(),
                coinsData.getDISPLAY().getUSD().getCHANGEDAY(),
                coinsData.getRAW().getUSD().getCHANGEDAY(),
                coinsData.getDISPLAY().getUSD().getCHANGEPCTDAY(),
                coinsData.getDISPLAY().getUSD().getSUPPLY(),
                coinsData.getDISPLAY().getUSD().getMKTCAP(),
                coinsData.getDISPLAY().getUSD().getVOLUME24HOUR(),
                coinsData.getDISPLAY().getUSD().getTOTALVOLUME24HTO(),
                coinsData.getDISPLAY().getUSD().getHIGHDAY(),
                coinsData.getDISPLAY().getUSD().getLOWDAY(),
                BASE_IMAGE_URL+coinsData.getCoinInfo().getUrl()
        );
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public Observable<ModelChart> getChartData(String symbol, String currency){
        return Network.getInstance().getApiCryptoCompare().getChartData(symbol,currency)
                .subscribeOn(Schedulers.io()) // "work" on io thread
                .observeOn(AndroidSchedulers.mainThread());
    }
}
