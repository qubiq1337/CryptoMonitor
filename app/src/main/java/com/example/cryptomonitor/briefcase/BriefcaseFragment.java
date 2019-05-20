package com.example.cryptomonitor.briefcase;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.cryptomonitor.activity.TransactionActivity;
import com.example.cryptomonitor.database.entities.Purchase;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.adapters.PortfolioAdapter;
import com.example.cryptomonitor.database.purchases.PurchaseAndCoin;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;


public class BriefcaseFragment extends Fragment implements View.OnClickListener, PortfolioAdapter.OnItemClickListener {


    private PortfolioAdapter portfolioAdapter;
    private PieChart mPieChart;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private BriefcaseViewModel mViewModel;
    public static final String COIN_INDEX = "COIN_INDEX";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_briefcase, container, false);
        FloatingActionButton plusButton = view.findViewById(R.id.floatingActionButton);
        plusButton.setOnClickListener(this);
        portfolioAdapter = new PortfolioAdapter(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.portfolio_recyclerView);
        recyclerView.setAdapter(portfolioAdapter);
        portfolioAdapter.setOnItemClickListener(this);
        mPieChart = view.findViewById(R.id.portfolio_pie_chart);
        initPieChart();
        mViewModel = ViewModelProviders.of(this).get(BriefcaseViewModel.class);
        mViewModel.getPieLiveData().observe(this, mPieEntryObserver);
        mViewModel.getPurchaseAndCoinLive().observe(this, mListObserver);
        mViewModel.getCurrenciesLiveData().observe(this, currenciesDataObserver);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Purchase purchase = portfolioAdapter.getmPortfolioItemList().get(viewHolder.getAdapterPosition()).getPurchase();
                mViewModel.removeSwipedItem(purchase);
            }
            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder.getAdapterPosition() % 2 == 0) return ItemTouchHelper.LEFT;
                else return ItemTouchHelper.RIGHT;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton:
                Intent intent = new Intent(this.getContext(), TransactionActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initPieChart() {
        mPieChart.setUsePercentValues(true);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(getResources().getColor(R.color.backgroundChartColor));
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

    private Observer<List<PieEntry>> mPieEntryObserver = this::setPieDataSet;

    private Observer<List<PurchaseAndCoin>> mListObserver = purchaseList -> {
        portfolioAdapter.setPortfolioItemList(purchaseList);
        portfolioAdapter.notifyDataSetChanged();
    };

    @Override
    public void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void OnItemClick(PurchaseAndCoin purchaseAndCoin) {
        Intent intent = new Intent(this.getContext(), TransactionActivity.class);
        intent.putExtra(COIN_INDEX, purchaseAndCoin.getPurchase().getPurchase_id());
        startActivity(intent);
    }

    private Observer<CurrenciesData> currenciesDataObserver = currenciesData ->
            portfolioAdapter.setCurrencies(currenciesData);
}
