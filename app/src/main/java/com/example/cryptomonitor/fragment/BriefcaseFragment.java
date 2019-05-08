package com.example.cryptomonitor.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.activity.BuyActivity;
import com.example.cryptomonitor.adapters.PortfolioAdapter;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.Purchase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


public class BriefcaseFragment extends Fragment implements View.OnClickListener {


    private FloatingActionButton mPlusButton;
    private PortfolioAdapter portfolioAdapter;
    private PieChart mPieChart;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_briefcase, container, false);
        mPlusButton = view.findViewById(R.id.floatingActionButton);
        mPlusButton.setOnClickListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.portfolio_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        portfolioAdapter = new PortfolioAdapter(getActivity());
        recyclerView.setAdapter(portfolioAdapter);

        mPieChart = view.findViewById(R.id.portfolio_pie_chart);
        initPieChart();
        getDataFromDb();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton:
                Intent intent = new Intent(this.getContext(), BuyActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getDataFromDb() {
        mCompositeDisposable.add(App.getDatabase()
                .purchaseDao()
                .getAll2()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(purchases ->
                        {
                            setItemsToPieChart(purchases);
                            portfolioAdapter.setmPotfolioItemList(purchases);
                            portfolioAdapter.notifyDataSetChanged();
                        },
                        e -> Log.e("OnError", e.toString())
                )
        );
    }

    private void setItemsToPieChart(List<Purchase> purchases) {

        mCompositeDisposable.add(
                Flowable.just(purchases)
                        .flatMap(Flowable::fromIterable)
                        .groupBy(Purchase::getCoinFullName)
                        .flatMapSingle(group ->
                                group.map(purchase -> purchase.getAmount() * purchase.getPrice())
                                        .reduce(0d, (Double x, Double y) -> x + y)
                                        .map(aDouble -> new PieEntry(aDouble.floatValue(), group.getKey() + ""))
                        )
                        .toList()
                        .subscribe(this::setPieDataSet)
        );
    }

    private void initPieChart() {
        mPieChart.setUsePercentValues(true);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.parseColor("#18142e"));
        mPieChart.animateY(800);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setTouchEnabled(false);

        Legend legend = mPieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setTextColor(Color.WHITE);
    }

    private void setPieDataSet(List<PieEntry> yValues) {
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setValueFormatter(new PercentFormatter(mPieChart));
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(4f);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        mPieChart.animateY(800);
        mPieChart.setData(pieData);
        mPieChart.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }

}
