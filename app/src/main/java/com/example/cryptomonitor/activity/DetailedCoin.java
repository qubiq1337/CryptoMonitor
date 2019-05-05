package com.example.cryptomonitor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.model_cryptocompare.model_chart.ChartData;
import com.example.cryptomonitor.model_cryptocompare.model_chart.ModelChart;
import com.example.cryptomonitor.network_api.NetworkHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

import static com.example.cryptomonitor.activity.MainActivity.EXTRA_CURRENCY_KEY;
import static com.example.cryptomonitor.activity.MainActivity.EXTRA_INDEX_KEY;
import static com.example.cryptomonitor.activity.MainActivity.EXTRA_POSITION_KEY;

public class DetailedCoin extends AppCompatActivity implements NetworkHelper.OnChangeRefreshingListener, View.OnClickListener {
    private LineChart lineChart;
    private List<String> dateXvalues = new ArrayList<>();
    private NetworkHelper networkHelper = new NetworkHelper(this);
    private String mIndex;
    private String mCurrency;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TextView full_name;
    private TextView price;
    private TextView change;
    private TextView supply;
    private TextView mkt;
    private TextView volume;
    private TextView total_volume;
    private TextView high;
    private TextView low;
    private ImageView infoURL;
    private ImageView backButton;
    private ImageView icon;
    private TextView textView_1D;
    private TextView textView_1W;
    private TextView textView_1M;
    private TextView textView_3M;
    private boolean activated_1d = true;
    private boolean activated_1w = true;
    private boolean activated_1m = true;
    private boolean activated_3m = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_coin);

        full_name = findViewById(R.id.detailed_fullname);
        price = findViewById(R.id.detailed_price);
        change = findViewById(R.id.detailed_change);
        mkt = findViewById(R.id.detailed_mkt);
        supply = findViewById(R.id.detailed_SPLY);
        volume = findViewById(R.id.detailed_volume);
        total_volume = findViewById(R.id.detailed_total_volume);
        TextView rank = findViewById(R.id.detailed_RANK);
        high = findViewById(R.id.detailed_high);
        low = findViewById(R.id.detailed_low);
        infoURL = findViewById(R.id.detailed_infoURL);
        backButton = findViewById(R.id.detailed_back);
        icon = findViewById(R.id.detailed_icon);
        textView_1D = findViewById(R.id.detailed_1D);
        textView_1D.setOnClickListener(this);
        textView_1W = findViewById(R.id.detailed_1W);
        textView_1W.setOnClickListener(this);
        textView_1M = findViewById(R.id.detailed_1M);
        textView_1M.setOnClickListener(this);
        textView_3M = findViewById(R.id.detailed_3M);
        textView_3M.setOnClickListener(this);

        Intent intent = getIntent();

        if (intent != null) {
            mIndex = intent.getStringExtra(EXTRA_INDEX_KEY);
            mCurrency = intent.getStringExtra(EXTRA_CURRENCY_KEY);
            int position = 0;
            String mRank = "#" + intent.getIntExtra(EXTRA_POSITION_KEY, position);
            rank.setText(mRank);
            initViews();
            initChart();
        }
    }

    private void initChart() {
        Log.e("Bundle", mCurrency + " " + mIndex);
        lineChart = findViewById(R.id.chart);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleYEnabled(false);
        lineChart.setScaleXEnabled(true);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        mCompositeDisposable.add(
                networkHelper
                        .getChartData1M(mIndex, mCurrency)
                        .map(this::dataVales)
                        .map(entries -> new LineDataSet(entries, "30 day"))
                        .subscribe(this::getSet, throwable -> Log.e("ER RX", throwable.toString()))
        );
    }

    private ArrayList<Entry> dataVales(@android.support.annotation.NonNull ModelChart modelChart) {
        ArrayList<Entry> dataVal1 = new ArrayList<Entry>();
        List<ChartData> coinData = modelChart.getData();
        dateXvalues.clear();
        int i = 0;
        for (ChartData data : coinData) {
            dataVal1.add(new Entry((float) i, data.getOpen().floatValue()));
            Date date = new Date();
            date.setTime(modelChart.getData().get(i).getTime().longValue() * 1000);
            SimpleDateFormat sm = new SimpleDateFormat("MMM-dd", Locale.US);
            String strDate = sm.format(date);
            dateXvalues.add(strDate);
            i++;
        }
        return dataVal1;
    }

    private void getSet(@android.support.annotation.NonNull LineDataSet set) {
        set.setDrawFilled(true);
        set.setColor(getResources().getColor(R.color.textColorPrimary));
        set.setFillColor(getResources().getColor(R.color.blue_color_selected));
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set);
        LineData data = new LineData(dataSets);
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        xAxis.setTextColor(getResources().getColor(R.color.textColorDark));
        yAxisLeft.setTextColor(getResources().getColor(R.color.textColorDark));
        yAxisRight.setTextColor(getResources().getColor(R.color.textColorDark));
        xAxis.setLabelCount(6, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return dateXvalues.get((int) value);
            }
        };
        xAxis.setValueFormatter(valueFormatter);
        lineChart.setData(data);
        lineChart.animateXY(1800, 1800);
        lineChart.invalidate();
    }

    private void initViews() {
        mCompositeDisposable.add(
                App
                        .getDatabase()
                        .coinInfoDao()
                        .getByShortName(mIndex)
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(coinInfoList -> !coinInfoList.isEmpty())
                        .flatMap(Flowable::fromIterable)
                        .subscribe(this::bindViews));
    }


    private void bindViews(@android.support.annotation.NonNull CoinInfo coinInfo) {
        full_name.setText(coinInfo.getFullName());
        Picasso.with(this).load(coinInfo.getImageURL()).into(icon);
        price.setText(coinInfo.getPriceDisplay());
        setChangeColor(coinInfo.getChangeDay());
        String changeConcat = coinInfo.getChangeDayDispaly() + " (" + coinInfo.getChangePctDay() + "%)";
        change.setText(changeConcat);
        mkt.setText(coinInfo.getMktcap());
        supply.setText(coinInfo.getSupply());
        volume.setText(coinInfo.getVolume());
        total_volume.setText(coinInfo.getTotalVolume24hTo());
        high.setText(coinInfo.getHigh());
        low.setText(coinInfo.getLow());
        infoURL.setOnClickListener(v ->
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(coinInfo.getInfoURL()));
            startActivity(browserIntent);
        });
    }

    private void setChangeColor(Double d) {
        if (d > 0)
            change.setTextColor(getResources().getColor(R.color.greenColor));
        else if (d < 0)
            change.setTextColor(getResources().getColor(R.color.redColor));
        else if (d == 0)
            change.setTextColor(getResources().getColor(R.color.textColorDark));
    }

    //No need
    @Override
    public void startRefreshing() {

    }

    //No need
    @Override
    public void stopRefreshing(boolean isSuccess) {

    }


    @NonNull
    @Override
    protected void onDestroy() {
        lineChart = null;
        dateXvalues = null;
        mCompositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detailed_1D:
                if (activated_1d) {
                    v.setBackground(getResources().getDrawable(R.drawable.rounded_text_view_selected));
                    textView_1W.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    textView_1M.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    textView_3M.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    mCompositeDisposable.add(networkHelper
                            .getChartData1D(mIndex, mCurrency)
                            .map(this::dataVales)
                            .map(entries -> new LineDataSet(entries, "30 day"))
                            .subscribe(this::getSet, throwable -> Log.e("getChartData1D", throwable.toString()))
                    );
                    activated_1d = false;
                    activated_1w = true;
                    activated_1m = true;
                    activated_3m = true;
                }
                break;
            case R.id.detailed_1W:
                if (activated_1w) {
                    v.setBackground(getResources().getDrawable(R.drawable.rounded_text_view_selected));
                    textView_1D.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    textView_1M.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    textView_3M.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    mCompositeDisposable.add(networkHelper
                            .getChartData1W(mIndex, mCurrency)
                            .map(this::dataVales)
                            .map(entries -> new LineDataSet(entries, "30 day"))
                            .subscribe(this::getSet, throwable -> Log.e("getChartData1W", throwable.toString()))
                    );
                    activated_1w = false;
                    activated_1d = true;
                    activated_1m = true;
                    activated_3m = true;
                }
                break;
            case R.id.detailed_1M:
                if (activated_1m) {
                    v.setBackground(getResources().getDrawable(R.drawable.rounded_text_view_selected));
                    textView_1D.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    textView_1W.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    textView_3M.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    mCompositeDisposable.add(networkHelper
                            .getChartData1M(mIndex, mCurrency)
                            .map(this::dataVales)
                            .map(entries -> new LineDataSet(entries, "30 day"))
                            .subscribe(this::getSet, throwable -> Log.e("getChartData1M", throwable.toString()))
                    );
                    activated_1m = false;
                    activated_1d = true;
                    activated_1w = true;
                    activated_3m = true;
                }
                break;
            case R.id.detailed_3M:
                if (activated_3m) {
                    v.setBackground(getResources().getDrawable(R.drawable.rounded_text_view_selected));
                    textView_1D.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    textView_1W.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    textView_1M.setBackground(getResources().getDrawable(R.drawable.rounded_text_view));
                    mCompositeDisposable.add(networkHelper
                            .getChartData3M(mIndex, mCurrency)
                            .map(this::dataVales)
                            .map(entries -> new LineDataSet(entries, "30 day"))
                            .subscribe(this::getSet, throwable -> Log.e("getChartData3M", throwable.toString()))
                    );
                    activated_3m = false;
                    activated_1d = true;
                    activated_1w = true;
                    activated_1m = true;
                }
                break;
        }
    }
}
