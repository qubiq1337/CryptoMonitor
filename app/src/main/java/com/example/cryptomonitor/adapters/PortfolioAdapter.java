package com.example.cryptomonitor.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.entities.Purchase;

import java.util.ArrayList;
import java.util.List;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder> {

    private Context mContex;
    private List<Purchase> mPotfolioItemList;

    public PortfolioAdapter(Context context) {
        mContex = context;
        mPotfolioItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PortfolioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContex).inflate(R.layout.portfolio_item_card, viewGroup, false);
        return new PortfolioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioViewHolder portfolioViewHolder, int i) {
        portfolioViewHolder.price.setText(mPotfolioItemList.get(i).getPriceDisplay());
        portfolioViewHolder.fullname.setText(mPotfolioItemList.get(i).getCoinFullName());
        String amountStr = mPotfolioItemList.get(i).getAmount() + " " + mPotfolioItemList.get(i).getCoinIndex();
        portfolioViewHolder.amount.setText(amountStr);
    }

    @Override
    public int getItemCount() {
        return mPotfolioItemList.size();
    }

    class PortfolioViewHolder extends RecyclerView.ViewHolder {

        TextView price;
        TextView amount;
        TextView fullname;

        public PortfolioViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.portfolio_price);
            amount = itemView.findViewById(R.id.portfolio_amount);
            fullname = itemView.findViewById(R.id.portfolio_fullname);
        }
    }

    public void setmPotfolioItemList(List<Purchase> mPotfolioItemList) {
        this.mPotfolioItemList = mPotfolioItemList;
    }
}
