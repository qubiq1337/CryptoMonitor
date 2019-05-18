package com.example.cryptomonitor.widget;

import android.accounts.NetworkErrorException;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinCryptoCompare;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinsData;
import com.example.cryptomonitor.network_api.Network;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<SmallCoin> mData = new ArrayList<>();
    private int mAppWidgetId;

    RemoteFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mData = App.getDatabase().coinInfoDao().getWidgetList().subscribeOn(Schedulers.io()).blockingGet();
    }

    //TODO: normal refreshing (method in NetApi)
    @Override
    public void onDataSetChanged() {
        ArrayList<CoinCryptoCompare> dataList = new ArrayList<>();
        try {
            for (int page = 1; page <= 19; page++) {
                Response<CoinCryptoCompare> response = Network.getInstance()
                        .getApiCryptoCompare().getAllCoinsToWidget(page, "USD").execute();
                CoinCryptoCompare coinCryptoCompare = null;
                if (response.body() != null)
                    coinCryptoCompare = response.body();
                else
                    throw new NetworkErrorException();
                dataList.add(coinCryptoCompare);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        List<CoinInfo> coinList = Observable.fromIterable(dataList)
                .map(CoinCryptoCompare::getData)
                .flatMap(Observable::fromIterable)
                .filter(coinsData -> coinsData.getRAW() != null && coinsData.getDISPLAY() != null)
                .map(this::toCoinInfo)
                .toList()
                .blockingGet();
        App.getDatabase().coinInfoDao().update(coinList);
        mData = App.getDatabase().coinInfoDao().getWidgetList().subscribeOn(Schedulers.io()).blockingGet();
    }

    @Override
    public void onDestroy() {
        mData = null;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        remoteViews.setTextViewText(R.id.name_tv, mData.get(position).getFullName());
        remoteViews.setTextViewText(R.id.price_tv, mData.get(position).getPriceDisplay());
        try {
            Bitmap bitmap = Picasso.with(mContext).load(mData.get(position).getImageURL()).get();
            remoteViews.setImageViewBitmap(R.id.coin_icon, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bundle extras = new Bundle();
        extras.putInt(WidgetProvider.POSITION_EXTRA, position + 1);
        //TODO: get normal currency
        extras.putString(WidgetProvider.SYMBOL_EXTRA, "USD");
        extras.putString(WidgetProvider.SHORT_NAME_EXTRA, mData.get(position).getShortName());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widget_layout, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private CoinInfo toCoinInfo(CoinsData coinsData) {
        final String baseImageUrl = "https://www.cryptocompare.com";
        return new CoinInfo(coinsData.getCoinInfo().getFullName(),
                coinsData.getCoinInfo().getName(),
                coinsData.getRAW().getUSD().getPRICE(), coinsData.getDISPLAY().getUSD().getPRICE(),
                coinsData.getDISPLAY().getUSD().getTOSYMBOL(),
                baseImageUrl + coinsData.getCoinInfo().getImageUrl(),
                coinsData.getDISPLAY().getUSD().getCHANGEDAY(),
                coinsData.getRAW().getUSD().getCHANGEDAY(),
                coinsData.getDISPLAY().getUSD().getCHANGEPCTDAY(),
                coinsData.getDISPLAY().getUSD().getSUPPLY(),
                coinsData.getDISPLAY().getUSD().getMKTCAP(),
                coinsData.getDISPLAY().getUSD().getVOLUME24HOUR(),
                coinsData.getDISPLAY().getUSD().getTOTALVOLUME24HTO(),
                coinsData.getDISPLAY().getUSD().getHIGHDAY(),
                coinsData.getDISPLAY().getUSD().getLOWDAY(),
                baseImageUrl + coinsData.getCoinInfo().getUrl()
        );
    }
}
