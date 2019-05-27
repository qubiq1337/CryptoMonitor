package com.example.cryptomonitor.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.purchases.PurchaseAndCoin;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;

import java.util.ArrayList;
import java.util.List;

import static com.example.cryptomonitor.Utilities.cashFormatting;
import static com.example.cryptomonitor.Utilities.simplePercentFormatting;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder> {


    private Context mContext;
    private List<PurchaseAndCoin> mPortfolioItemList;
    private CurrenciesData currencies;
    private OnItemClickListener onItemClickListener;

    public PortfolioAdapter(Context context) {
        mContext = context;
        mPortfolioItemList = new ArrayList<>();
        currencies = new CurrenciesData();
    }

    public List<PurchaseAndCoin> getmPortfolioItemList() {
        return mPortfolioItemList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

        Double buyPrice = purchaseAndCoin.getPurchase().getPrice_purchase();
        Double currentPrice = purchaseAndCoin.getDouble_price();
        String buyCurrency = purchaseAndCoin.getPurchase().getBuyCurrency();

        Double change = getChangePercent(buyPrice, currentPrice, buyCurrency);
        change = simplePercentFormatting(change);
        String strChange = change + "%";
        //Fix -0.0 в отображении change
        if (change == 0D) strChange = strChange.replace("-", "");
        portfolioViewHolder.change.setText(strChange);
        portfolioViewHolder.change.setTextColor(changeColor(change));
        portfolioViewHolder.change.setTextSize(TypedValue.COMPLEX_UNIT_SP, changeTextSize(change));
        String buyPriceFormatted = purchaseAndCoin.getPurchase().getBuyCurrencySymbol() + " " + cashFormatting(buyPrice);
        portfolioViewHolder.price.setText(buyPriceFormatted);

        portfolioViewHolder.fullName.setText(purchaseAndCoin.getCoinFullName());

        String amountStr = cashFormatting(purchaseAndCoin.getPurchase().getAmount()) + " " + purchaseAndCoin.getCoinIndex();
        portfolioViewHolder.amount.setText(amountStr);
    }

    @Override
    public int getItemCount() {
        return mPortfolioItemList.size();
    }

    public void setPortfolioItemList(List<PurchaseAndCoin> mPortfolioItemList) {
        this.mPortfolioItemList = mPortfolioItemList;
    }

    private Double getBuyCurrencyPrice(String buyCurrency) {
        switch (buyCurrency) {
            case ("USD"):
                return currencies.getUSD();
            case ("EUR"):
                return currencies.getEUR();
            case ("CNY"):
                return currencies.getCNY();
            case ("RUB"):
                return currencies.getRUB();
            case ("GBP"):
                return currencies.getGBP();
        }
        return 1D;
    }

    private Double convert(Double currentPrice, Double buyCurrencyPrice) {
        if (buyCurrencyPrice == null) {
            return 1D;
        } else {
            return currentPrice * buyCurrencyPrice;
        }
    }

    private Double change(Double buyPrice, Double convertedCurrentPrice) {
        double numerator = convertedCurrentPrice - buyPrice;
        if (numerator >= 0) return (convertedCurrentPrice - buyPrice) / buyPrice * 100D;
        else return (convertedCurrentPrice - buyPrice) / convertedCurrentPrice * 100D;
    }

    private Double getChangePercent(Double buyPrice, Double currentPrice, String buyCurrency) {
        Double buyCurrencyPrice = getBuyCurrencyPrice(buyCurrency);
        return change(buyPrice, convert(currentPrice, buyCurrencyPrice));
    }

    public void setCurrencies(CurrenciesData currencies) {
        this.currencies = currencies;
    }

    private int changeColor(Double d) {
        if (d > 0)
            return (ContextCompat.getColor(mContext, R.color.green5));
        else if (d < 0)
            return (ContextCompat.getColor(mContext, R.color.red1));
        return (Color.GRAY);
    }

    private int changeTextSize(Double change) {
        if (change < 0) change = change * (-1);
        if (change < 10) return 24;
        else if (change >= 10 && change < 1000) return 20;
        else if (change >= 1000 && change < 10000) return 18;
        return 16;
    }

    public interface OnItemClickListener {
        void OnItemClick(PurchaseAndCoin purchaseAndCoin);
    }

    class PortfolioViewHolder extends RecyclerView.ViewHolder {

        TextView price;
        TextView amount;
        TextView fullName;
        TextView change;

        PortfolioViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.portfolio_price);
            amount = itemView.findViewById(R.id.portfolio_amount);
            fullName = itemView.findViewById(R.id.portfolio_fullname);
            change = itemView.findViewById(R.id.portfolio_change);
            itemView.setOnClickListener(v -> {
                onItemClickListener.OnItemClick(mPortfolioItemList.get(getAdapterPosition()));
            });
        }
    }

}
