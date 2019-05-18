package com.example.cryptomonitor.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.activity.DetailedCoin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WidgetProvider extends AppWidgetProvider {
    public static final String SYMBOL_EXTRA = "symbolExtra";
    public static final String POSITION_EXTRA = "positionExtra";
    public static final String SHORT_NAME_EXTRA = "shortNameExtra";

    final static String ACTION_ON_CLICK = "onItemClick";
    final static String ITEM_POSITION = "itemPosition";

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
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
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
        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
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
}
