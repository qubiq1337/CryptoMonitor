package com.example.cryptomonitor.briefcase;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.adapters.PortfolioAdapter;
import com.example.cryptomonitor.database.purchases.Purchase;
import com.example.cryptomonitor.database.purchases.PurchaseAndCoin;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;
import com.example.cryptomonitor.transaction.TransactionActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;


public class BriefcaseFragment extends Fragment implements View.OnClickListener, PortfolioAdapter.OnItemClickListener {


    public static final String COIN_INDEX = "COIN_INDEX";
    private PortfolioAdapter portfolioAdapter;
    private PieChart mPieChart;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private BriefcaseViewModel mViewModel;
    private Observer<List<PieEntry>> mPieEntryObserver = this::setPieDataSet;
    private Observer<List<PurchaseAndCoin>> mListObserver = purchaseList -> {
        portfolioAdapter.setPortfolioItemList(purchaseList);
        portfolioAdapter.notifyDataSetChanged();
    };
    private Observer<CurrenciesData> currenciesDataObserver = currenciesData ->
            portfolioAdapter.setCurrencies(currenciesData);

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
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    if (viewHolder.getAdapterPosition() % 2 == 0) return ItemTouchHelper.LEFT;
                    else return ItemTouchHelper.RIGHT;
                }
                return ItemTouchHelper.LEFT;

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floatingActionButton) {
            Intent intent = new Intent(this.getContext(), TransactionActivity.class);
            startActivity(intent);
        }
    }

    private void initPieChart() {
        mPieChart.setUsePercentValues(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mPieChart.setExtraOffsets(35, 5, 35, 5);
        else
            mPieChart.setExtraOffsets(35, 15, 35, 15);
        mPieChart.setDrawHoleEnabled(true);
        //TODO refactor
        mPieChart.setHoleColor(ContextCompat.getColor(Objects.requireNonNull(this.getContext()), R.color.dark1));
        mPieChart.animateY(800);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setTouchEnabled(true);
        mPieChart.setDragDecelerationFrictionCoef(0.1f);
        Legend legend = mPieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setTextColor(Color.WHITE);
    }

    private void setPieDataSet(List<PieEntry> yValues) {
        if (yValues.isEmpty()) mPieChart.setDrawHoleEnabled(false);
        else mPieChart.setDrawHoleEnabled(true);
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setValueFormatter(new PercentFormatter(mPieChart));
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(4f);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLineColor(Color.GRAY);
        pieDataSet.setValueLinePart1OffsetPercentage(100f); // When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size
        pieDataSet.setValueLinePart1Length(0.6f); // When valuePosition is OutsideSlice, indicates length of first half of the line
        pieDataSet.setValueLinePart2Length(0.4f); // When valuePosition is OutsideSlice, indicates length of second half of the line
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        mPieChart.setDrawEntryLabels(false);
        mPieChart.animateY(800);
        mPieChart.setData(pieData);
        mPieChart.notifyDataSetChanged();
    }

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
}
