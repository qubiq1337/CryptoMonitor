package com.example.cryptomonitor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
    private List<CoinInfo> coinInfoList;
    private List<CoinInfo> reusultFilterList;
    private Context mContex;



    public AutoCompleteAdapter(Context context) {
        mContex = context;
        reusultFilterList = new ArrayList<>();
        coinInfoList = new ArrayList<>();

        Disposable disposable = App
                .getDatabase()
                .coinInfoDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    coinInfoList = list;
                    Log.e("findCoin",coinInfoList.size()+"");
                });
    }

    @Override
    public int getCount() {
        return reusultFilterList.size();
    }

    @Override
    public CoinInfo getItem(int index) {
        return reusultFilterList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContex).inflate(R.layout.autocomplete_tv_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.autocomplete_item_full_name);
        ImageView imageView = convertView.findViewById(R.id.autocomplete_item_icon);
        CoinInfo coinInfo = getItem(position);

        if (coinInfo != null) {
            textView.setText(coinInfo.getFullName());
            Picasso.with(mContex).load(coinInfo.getImageURL()).into(imageView);
        }

        return convertView;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                List<CoinInfo> suggestions = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(coinInfoList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (CoinInfo item : coinInfoList) {
                        if (item.getFullName().toLowerCase().contains(filterPattern)) {
                            suggestions.add(item);
                        }
                    }
                }
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    reusultFilterList = (List<CoinInfo>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((CoinInfo) resultValue).getFullName();
            }

        };
        return filter;
    }
}
