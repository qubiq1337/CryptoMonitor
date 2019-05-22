package com.example.cryptomonitor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MinCoinAdapter extends RecyclerView.Adapter<MinCoinAdapter.MinCoinViewHolder> {

    private static final String ICONS_MASTER_32_X_32 = "https://raw.githubusercontent.com/MoneyConverter/cryptocurrencies-icons/master/32x32/";
    private Context mContext;
    private OnItemClickListener mOnClickListener;
    private List<CoinInfo> mData = new ArrayList<>();

    public MinCoinAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mOnClickListener = onItemClickListener;
    }

    public void setData(List<CoinInfo> coinInfoList) {
        mData = coinInfoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MinCoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.min_coin_item, viewGroup, false);
        return new MinCoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MinCoinViewHolder minCoinViewHolder, int i) {
        final CoinInfo coinInfo = mData.get(i);
        String URL = ICONS_MASTER_32_X_32 + mData.get(i).getShortName().toLowerCase() + ".png";
        Picasso.with(mContext).load(URL).into(minCoinViewHolder.mIconImage);
        minCoinViewHolder.mCoinNameTv.setText(coinInfo.getFullName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(CoinInfo coinInfo);
    }

    class MinCoinViewHolder extends RecyclerView.ViewHolder {
        private TextView mCoinNameTv;
        private ImageView mIconImage;

        MinCoinViewHolder(@NonNull View itemView) {
            super(itemView);
            mCoinNameTv = itemView.findViewById(R.id.item_name);
            mIconImage = itemView.findViewById(R.id.item_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.OnItemClick(mData.get(getAdapterPosition()));
                }
            });
        }
    }
}
