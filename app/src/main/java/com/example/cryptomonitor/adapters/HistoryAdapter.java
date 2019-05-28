package com.example.cryptomonitor.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.bills.Bill;
import com.example.cryptomonitor.home.CoinAdapterHome;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.cryptomonitor.Utilities.cashFormatting;
import static com.example.cryptomonitor.Utilities.simpleNumberFormatting;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<Bill> mBillList;
    private Context mContext;

    public HistoryAdapter(Context context) {
        mContext = context;
        mBillList = new ArrayList<>();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Bill bill = mBillList.get(position);
        holder.name.setText(bill.getFull_name());
        holder.sellDate.setText(bill.getSell_date());
        holder.buyDate.setText(bill.getBuy_date());
        double buyPrice = bill.getBuyPrice();
        double sellPrice = bill.getSellPrice();
        double change = changePercent(buyPrice, sellPrice);
        String changeInPercent = simpleNumberFormatting(change) + "%";
        holder.change.setText(changeInPercent);
        holder.change.setTextSize(TypedValue.COMPLEX_UNIT_SP, changeTextSize(change));
        holder.change.setTextColor(changeColor(change));
        String sellPriceFormatted = bill.getBuy_currency_symbol()+" "+cashFormatting(sellPrice);
        String buyPriceFormatted = bill.getBuy_currency_symbol()+" "+cashFormatting(buyPrice);
        holder.sellPrice.setText(sellPriceFormatted);
        holder.buyPrice.setText(buyPriceFormatted);
        String amountStr = cashFormatting(bill.getAmount()) + " " + bill.getShort_name();
        holder.amount.setText(amountStr);
        Picasso.get()
                .load(bill.getImage_url())
                .transform(new CoinAdapterHome.PicassoCircleTransformation())
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return mBillList.size();
    }

    public void setBillList(List<Bill> mBillList) {
        this.mBillList = mBillList;
        notifyDataSetChanged();
    }

    private Double changePercent(Double buyPrice, Double sellPrice) {
        double numerator = sellPrice - buyPrice;
        if (numerator >= 0) return (sellPrice - buyPrice) / buyPrice * 100D;
        else return (sellPrice - buyPrice) / sellPrice * 100D;
    }

    private int changeColor(Double d) {
        if (d > 0)
            return (ContextCompat.getColor(mContext, R.color.green5));
        else if (d < 0)
            return (ContextCompat.getColor(mContext, R.color.red1));
        return (Color.GRAY);
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView buyPrice;
        private TextView sellPrice;
        private TextView buyDate;
        private TextView sellDate;
        private TextView amount;
        private TextView name;
        private ImageView icon;
        private TextView change;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            buyPrice = itemView.findViewById(R.id.history_buy_price);
            sellPrice = itemView.findViewById(R.id.history_sell_price);
            buyDate = itemView.findViewById(R.id.history_buy_date);
            sellDate = itemView.findViewById(R.id.history_sell_date);
            amount = itemView.findViewById(R.id.history_amount);
            name = itemView.findViewById(R.id.history_item_name);
            icon = itemView.findViewById(R.id.history_item_icon);
            change = itemView.findViewById(R.id.history_change);
        }
    }

    private int changeTextSize(Double change) {
        if (change < 0) change = change * (-1);
        if (change < 10) return 36;
        else if (change >= 10 && change < 1000) return 32;
        else if (change >= 1000 && change < 10000) return 26;
        return 22;
    }
}
