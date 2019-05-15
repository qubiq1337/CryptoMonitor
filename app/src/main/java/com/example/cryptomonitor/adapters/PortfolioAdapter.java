package com.example.cryptomonitor.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.purchases.PurchaseAndCoin;

import java.util.ArrayList;
import java.util.List;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder> {

    private Context mContext;
    private List<PurchaseAndCoin> mPortfolioItemList;

    public PortfolioAdapter(Context context) {
        mContext = context;
        mPortfolioItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.portfolio_item_card, viewGroup, false);
        return new PortfolioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortfolioViewHolder portfolioViewHolder, int i) {
        PurchaseAndCoin purchaseAndCoin = mPortfolioItemList.get(i);
        portfolioViewHolder.price.setText(purchaseAndCoin.getPriceDisplay());
        portfolioViewHolder.fullName.setText(purchaseAndCoin.getCoinFullName());
        String amountStr = purchaseAndCoin.getPurchase().getAmount() + " " + purchaseAndCoin.getCoinIndex();
        portfolioViewHolder.amount.setText(amountStr);
    }

    @Override
    public int getItemCount() {
        return mPortfolioItemList.size();
    }

    public void setPortfolioItemList(List<PurchaseAndCoin> mPortfolioItemList) {
        this.mPortfolioItemList = mPortfolioItemList;
    }

    class PortfolioViewHolder extends RecyclerView.ViewHolder {

        TextView price;
        TextView amount;
        TextView fullName;

        PortfolioViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.portfolio_price);
            amount = itemView.findViewById(R.id.portfolio_amount);
            fullName = itemView.findViewById(R.id.portfolio_fullname);
        }
    }
}
