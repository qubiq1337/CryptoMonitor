package com.example.cryptomonitor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.cryptomonitor.activity.MainActivity.EXTRA_CURRENCY_KEY;
import static com.example.cryptomonitor.activity.MainActivity.EXTRA_INDEX_KEY;
import static com.example.cryptomonitor.activity.MainActivity.EXTRA_POSITION_KEY;

public class DetailedCoin extends AppCompatActivity implements NetworkHelper.OnChangeRefreshingListener {
    private LineChart lineChart;
    private List<String> dateXvalues = new ArrayList<>();
    private NetworkHelper networkHelper = new NetworkHelper(this);
    private String mIndex;
    private String mCurrency;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TextView fullname;
    private TextView price;
    private TextView change;
    private TextView supply;
    private TextView mkt;
    private TextView volume;
    private TextView total_volme;
    private TextView rank;
    private TextView high;
    private TextView low;
    private ImageView infoURL;
    private ImageView backButton;
    private ImageView icon;
    private String mRank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_coin);

        fullname = findViewById(R.id.detailed_fullname);
        price = findViewById(R.id.detailed_price);
        change = findViewById(R.id.detailed_change);
        mkt = findViewById(R.id.detailed_mkt);
        supply = findViewById(R.id.detailed_SPLY);
        volume = findViewById(R.id.detailed_volume);
        total_volme = findViewById(R.id.detailed_total_volume);
        rank = findViewById(R.id.detailed_RANK);
        high = findViewById(R.id.detailed_high);
        low = findViewById(R.id.detailed_low);
        infoURL = findViewById(R.id.detailed_infoURL);
        backButton = findViewById(R.id.detailed_back);
        icon = findViewById(R.id.detailed_icon);

        Intent intent = getIntent();
        if (intent != null) {
            mIndex = intent.getStringExtra(EXTRA_INDEX_KEY);
            mCurrency = intent.getStringExtra(EXTRA_CURRENCY_KEY);
            int position = 0;
            mRank = "#"+intent.getIntExtra(EXTRA_POSITION_KEY,position);
            initViews();
            initChart();
        }

        rank.setText(mRank);
    }

    private void initChart() {
        Log.e("Bundle", mCurrency + " " + mIndex);
        lineChart = findViewById(R.id.chart);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleYEnabled(false);
        lineChart.setScaleXEnabled(true);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.animateXY(2000, 2000);

        mCompositeDisposable.add(networkHelper.getChartData(mIndex, mCurrency)
                .map(this::dataVales)
                .map(entries -> {
                    return new LineDataSet(entries, "30 day");
                })
                .subscribe(this::getSet, throwable -> Log.e("ER RX", throwable.toString()))
        );
    }

    private ArrayList<Entry> dataVales(ModelChart modelChart) {
        ArrayList<Entry> dataVal1 = new ArrayList<Entry>();
        List<ChartData> coinData = modelChart.getData();
        int i = 0;
        for (ChartData data : coinData) {
            dataVal1.add(new Entry((float) i, data.getOpen().floatValue()));
            Date date = new Date();
            date.setTime(modelChart.getData().get(i).getTime().longValue() * 1000);
            SimpleDateFormat sm = new SimpleDateFormat("MMM-d", Locale.US);
            String strDate = sm.format(date);
            dateXvalues.add(strDate);
            i++;
        }
        return dataVal1;
    }

    private void getSet(LineDataSet set) {
        set.setDrawFilled(true);
        set.setColor(Color.parseColor("#ffffff"));
        set.setFillColor(Color.parseColor("#007ff2"));
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
        xAxis.setTextColor(Color.parseColor("#c7c7c7"));
        yAxisLeft.setTextColor(Color.parseColor("#c7c7c7"));
        yAxisRight.setTextColor(Color.parseColor("#c7c7c7"));
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
        lineChart.invalidate();
    }

    private void initViews() {

        mCompositeDisposable.add(App.getDatabase()
                .coinInfoDao()
                .getByShortName(mIndex)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(coinInfoList -> !coinInfoList.isEmpty())
                .map(coinInfoList -> coinInfoList.get(0))
                .subscribe(this::bindViews));
    }

    private void bindViews(CoinInfo coinInfo){
        fullname.setText(coinInfo.getFullName());
        Picasso.with(this).load(coinInfo.getImageURL()).into(icon);
        price.setText(coinInfo.getPriceDisplay());
        setChangeColor(coinInfo.getChangeDay());
        String changeConcat = coinInfo.getChangeDayDispaly()+" ("+coinInfo.getChangePctDay()+"%)";
        change.setText(changeConcat);
        mkt.setText(coinInfo.getMktcap());
        supply.setText(coinInfo.getSupply());
        volume.setText(coinInfo.getVolume());
        total_volme.setText(coinInfo.getTotalVolume24hTo());
        high.setText(coinInfo.getHigh());
        low.setText(coinInfo.getLow());
        infoURL.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(coinInfo.getInfoURL()));
            startActivity(browserIntent);
        });
    }

    private void setChangeColor(Double d){
        if (d > 0)
        change.setTextColor(getResources().getColor(R.color.greenColor));
        else if (d < 0)
            change.setTextColor(getResources().getColor(R.color.redColor));
        else if (d == 0)
            change.setTextColor(getResources().getColor(R.color.textColorDark));
    }


    @Override
    public void startRefreshing() {

    }

    @Override
    public void stopRefreshing(boolean isSuccess) {

    }

    @NonNull
    @Override
    protected void onDestroy() {
        lineChart = null;
        dateXvalues = null;
        dateXvalues = null;
        mCompositeDisposable.dispose();
        super.onDestroy();
    }
}
