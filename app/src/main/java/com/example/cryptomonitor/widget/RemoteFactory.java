package com.example.cryptomonitor.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.App;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        mData = App.getDatabase().coinInfoDao().getWidgetList();
    }

    @Override
    public void onDataSetChanged() {
        mData = App.getDatabase().coinInfoDao().getWidgetList();
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
        extras.putInt(WidgetProvider.POSITION_EXTRA, position);
        extras.putString(WidgetProvider.SYMBOL_EXTRA, mData.get(position).getSymbol());
        extras.putString(WidgetProvider.SHORT_NAME_EXTRA, mData.get(position).getShortName());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.layout.widget_list_item, fillInIntent);

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
}
