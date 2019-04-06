package com.example.cryptomonitor.network_api;

import android.util.Log;
import android.widget.Toast;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.CoinInfo;
import com.example.cryptomonitor.database.CoinInfoDao;
import com.example.cryptomonitor.model.CoinCryptoCompare;
import com.example.cryptomonitor.model.Datum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkHelper {

    private final int start_limit = 100;
    private final int statr_page = 0;
    private boolean refreshing = true;

    public void start(String curency) {
        loadCoins(start_limit, statr_page, curency);
    }

    private void loadCoins(int limit, int page, String curency) {
        Network.getInstance()
                .getApiCryptoCompare()
                .getTopListData(limit, page, curency)
                .enqueue(new Callback<CoinCryptoCompare>() {
                    @Override
                    public void onResponse(Call<CoinCryptoCompare> call, Response<CoinCryptoCompare> response) {
                        if (response.body() != null) {
                            updateDatabase(getCoinInfoList(response.body()));
                            refreshing = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<CoinCryptoCompare> call, Throwable t) {
                        Log.e("LOAD_COINs", t.toString());
                        refreshing = false;
                    }
                });
    }

    private List<CoinInfo> getCoinInfoList(CoinCryptoCompare mCoinCryptoCompare) {
        List<CoinInfo> coinInfoArrayList = new ArrayList<>();
        List<Datum> coinCryptoCompareData = mCoinCryptoCompare.getData();
        CoinInfo coinInfo;
        for (Datum coin : coinCryptoCompareData) {
            String fullName = coin.getCoinInfo().getFullName();
            String shortName = coin.getCoinInfo().getName();
            String price = coin.getDISPLAY().getUSD().getPRICE();
            String imageURL = coin.getCoinInfo().getImageUrl();
            coinInfo = new CoinInfo(fullName, shortName, price, imageURL);
            coinInfoArrayList.add(coinInfo);
        }
        return coinInfoArrayList;
    }

    /**
     * к базе данных следует обращаться из другого потока, здесь мы можем этого не делать,
     * потому что метод вызывается в network api, а он работает уже в другом потоке
     */
    private void updateDatabase(List<CoinInfo> newCoinInfoList) {
        CoinInfoDao coinInfoDao = App.getDatabase().coinInfoDao();
        List<CoinInfo> insertList = new ArrayList<>();
        List<CoinInfo> updateList = new ArrayList<>();
        for (CoinInfo coinInfo : newCoinInfoList) {
            List<CoinInfo> dbInfoList = coinInfoDao.getByFullName(coinInfo.getFullName());    // получаем список, чтобы если нет записи,
            if (dbInfoList.isEmpty()) {                                            // пришел хотя бы пустой список, это значит что ее надо добавить
                insertList.add(coinInfo);
            } else {
                CoinInfo dbCoinInfo = dbInfoList.get(0);                                    //если список не пустой, там одна запись по нужному id
                coinInfo.setCoinId(dbCoinInfo.getCoinId());
                coinInfo.setFavorite(dbCoinInfo.isFavorite());
                updateList.add(coinInfo);
            }
        }
        coinInfoDao.insert(insertList);
        coinInfoDao.update(updateList);
    }

    public boolean isRefreshing() {
        return refreshing;
    }

    public void loadNextCoin(int page,String curency){
        loadCoins(start_limit,page,curency);
    }
}
