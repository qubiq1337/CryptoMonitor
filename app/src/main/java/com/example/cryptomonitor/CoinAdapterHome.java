package com.example.cryptomonitor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cryptomonitor.model.CoinCryptoCompare;
import com.squareup.picasso.Picasso;

public class CoinAdapterHome extends RecyclerView.Adapter<CoinAdapterHome.CoinViewHolder> {

    private CoinCryptoCompare mCoinCryptoCompare;
    private Context mContext;
    private final String URL = "https://www.cryptocompare.com";

    public CoinAdapterHome(CoinCryptoCompare coinCryptoCompare, Context context){
        mCoinCryptoCompare=coinCryptoCompare;
        mContext=context;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.rv_coin_layuot, viewGroup, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder coinViewHolder, int i) {
        coinViewHolder.textViewFullName.setText(mCoinCryptoCompare.getData().get(i).getCoinInfo().getFullName());
        coinViewHolder.textViewName.setText(mCoinCryptoCompare.getData().get(i).getCoinInfo().getName());
        coinViewHolder.textViewPrice.setText(mCoinCryptoCompare.getData().get(i).getDISPLAY().getUSD().getPRICE());
        Picasso.with(mContext).load(URL + mCoinCryptoCompare.getData().get(i).getCoinInfo().getImageUrl()).into(coinViewHolder.imageViewIcon);
    }

    @Override
    public int getItemCount() {
        return mCoinCryptoCompare.getData().size();
    }

    class CoinViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPrice;
        private TextView textViewFullName;
        private TextView textViewName;
        private ImageView imageViewIcon;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFullName = itemView.findViewById(R.id.rv_coin_layout_fullname);
            textViewName = itemView.findViewById(R.id.rv_coin_layout_name);
            textViewPrice = itemView.findViewById(R.id.rv_coin_layout_price);
            imageViewIcon = itemView.findViewById(R.id.rv_coin_layout_icon);
        }
    }

}
