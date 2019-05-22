package com.example.cryptomonitor.widget;

import android.accounts.NetworkErrorException;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.activity.DetailedCoin;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.coins.CoinInfo;
import com.example.cryptomonitor.database.coins.CoinInfoDao;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinCryptoCompare;
import com.example.cryptomonitor.model_cryptocompare.model_coins.CoinsData;
import com.example.cryptomonitor.network_api.Network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import retrofit2.Response;

public class WidgetProvider extends AppWidgetProvider {
    public static final String SYMBOL_EXTRA = "symbolExtra";
    public static final String POSITION_EXTRA = "positionExtra";
    public static final String SHORT_NAME_EXTRA = "shortNameExtra";

    final static String ACTION_ON_CLICK = "onItemClick";
    public static final String ACTION_SHOW_TOAST = "showToast";
    public static final String EXTRA_MESSAGE = "extraMessage";
    public static final String ACTION_UPDATE_DB = "updateDb";
    private CoinInfoDao dao = App.getDatabase().coinInfoDao();


    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault());

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(ACTION_ON_CLICK)) {
            Intent activityIntent = new Intent(context, DetailedCoin.class);
            int position = intent.getIntExtra(POSITION_EXTRA, -1);
            String index = intent.getStringExtra(SHORT_NAME_EXTRA);
            String currency = intent.getStringExtra(SYMBOL_EXTRA);
            activityIntent.putExtra(DetailedCoin.EXTRA_POSITION_KEY, position);
            activityIntent.putExtra(DetailedCoin.EXTRA_INDEX_KEY, index);
            activityIntent.putExtra(DetailedCoin.EXTRA_CURRENCY_KEY, currency);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }

        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(ACTION_SHOW_TOAST)) {
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

        if (intent.getAction() != null && intent.getAction().equals(ACTION_UPDATE_DB)) {
            Toast.makeText(context, context.getString(R.string.updating), Toast.LENGTH_SHORT).show();
            int[] ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            updateDB(context, ids);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int id) {

        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
        String timeStr = context.getString(R.string.updated_at) + " " + mDateFormat.format(new Date(System.currentTimeMillis()));
        widgetView.setTextViewText(R.id.time_tv, timeStr);
        widgetView.setRemoteAdapter(R.id.widget_list, serviceIntent);

        Intent updIntent = new Intent(context, WidgetProvider.class);
        updIntent.setAction(ACTION_UPDATE_DB);
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{id});
        PendingIntent updPIntent = PendingIntent.getBroadcast(context, id, updIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.refresh_btn, updPIntent);

        Intent listClickIntent = new Intent(context, WidgetProvider.class);
        listClickIntent.setAction(ACTION_ON_CLICK);
        PendingIntent listClickPIntent = PendingIntent.getBroadcast(context, 0,
                listClickIntent, 0);
        widgetView.setPendingIntentTemplate(R.id.widget_list, listClickPIntent);

        appWidgetManager.updateAppWidget(id, widgetView);
        appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.widget_list);

        Toast.makeText(context, context.getString(R.string.update_success), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    private void updateDB(Context context, int[] ids) {
        AppExecutors.getInstance().getDbExecutor().execute(() -> {
            ArrayList<CoinCryptoCompare> dataList = new ArrayList<>();
            try {
                for (int page = 0; page <= 19; page++) {
                    Response<CoinCryptoCompare> response = Network.getInstance()
                            .getApiCryptoCompare().getAllCoinsToWidget(page, "USD").execute();
                    CoinCryptoCompare coinCryptoCompare;
                    if (response.body() != null)
                        coinCryptoCompare = response.body();
                    else
                        throw new NetworkErrorException();
                    dataList.add(coinCryptoCompare);
                }
            } catch (Exception e) {
                AppExecutors.getInstance().getMainThreadExecutor().execute(() ->
                        Toast.makeText(context, context.getString(R.string.updating_failed), Toast.LENGTH_SHORT).show());
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
            updateAll(coinList);
            Intent updIntent = new Intent(context, WidgetProvider.class);
            updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            PendingIntent updPIntent = PendingIntent.getBroadcast(context, 0, updIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                updPIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        });
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
                baseImageUrl + coinsData.getCoinInfo().getUrl(),
                coinsData.getRAW().getUSD().getMKTCAP()
        );
    }

    private void updateAll(List<CoinInfo> coinInfoList) {
        List<CoinInfo> insertList = new ArrayList<>();
        List<CoinInfo> updateList = new ArrayList<>();
        for (CoinInfo coinInfo : coinInfoList) {
            List<CoinInfo> dbInfoList = dao.getByFullName(coinInfo.getFullName());
            if (dbInfoList.isEmpty()) {
                insertList.add(coinInfo);
            } else {
                CoinInfo dbCoinInfo = dbInfoList.get(0);
                coinInfo.setId(dbCoinInfo.getId());
                coinInfo.setFavorite(dbCoinInfo.isFavorite());
                updateList.add(coinInfo);
            }
        }
        dao.insert(insertList);
        dao.update(updateList);
    }
}
