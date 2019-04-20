package com.example.cryptomonitor.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.entities.CoinInfo;

import java.util.ArrayList;
import java.util.List;

public class MinCoinAdapter extends RecyclerView.Adapter<MinCoinAdapter.MinCoinViewHolder> {

    private List<CoinInfo> mData;
    private Context mContext;
    private OnItemClickListener mOnClickListener;

    public interface OnItemClickListener {
        void OnItemClick(CoinInfo coinInfo);
    }

    public MinCoinAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mOnClickListener = onItemClickListener;
        mData = new ArrayList<>();
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
        minCoinViewHolder.mCoinNameTv.setText(coinInfo.getFullName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<CoinInfo> list) {
        mData = list;
        notifyDataSetChanged();
    }

    class MinCoinViewHolder extends RecyclerView.ViewHolder {
        private TextView mCoinNameTv;

        MinCoinViewHolder(@NonNull View itemView) {
            super(itemView);
            mCoinNameTv = itemView.findViewById(R.id.rv_item_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.OnItemClick(mData.get(getAdapterPosition()));
                }
            });
        }
    }
}
