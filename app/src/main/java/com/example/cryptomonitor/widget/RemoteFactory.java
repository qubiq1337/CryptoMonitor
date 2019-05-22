package com.example.cryptomonitor.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.coins.CoinInfoDao;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class RemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<SmallCoin> mData = new ArrayList<>();
    private CoinInfoDao dao = App.getDatabase().coinInfoDao();

    RemoteFactory(Context context, Intent intent) {
        mContext = context;
    }

    //initial data
    @Override
    public void onCreate() {
        mData = dao.getWidgetList().subscribeOn(Schedulers.io()).blockingGet();
    }

    //prepare changed data
    @Override
    public void onDataSetChanged() {
        mData = dao.getWidgetList().subscribeOn(Schedulers.io()).blockingGet();
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
            Bitmap bitmap = Picasso.with(mContext)
                    .load(mData.get(position).getImageURL())
                    .error(R.drawable.ic_action_close)
                    .get();
            remoteViews.setImageViewBitmap(R.id.coin_icon, bitmap);
        } catch (Exception e) {
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

}
