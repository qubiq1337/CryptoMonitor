package com.example.cryptomonitor.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CoinAdapterHome extends RecyclerView.Adapter<CoinAdapterHome.CoinViewHolder> {


    private Context mContext;
    private List<CoinInfo> mData;
    private OnStarClickListener mOnStarClickListener;
    private OnEndReachListener mOnEndReachListener;
    private boolean isLoading;

    CoinAdapterHome(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
    }


    void setup(Fragment fragment) {
        this.mOnStarClickListener = (OnStarClickListener) fragment;
        this.mOnEndReachListener = (OnEndReachListener) fragment;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_coin_layout, viewGroup, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder coinViewHolder, int i) {
        CoinInfo coin = mData.get(i);
        coinViewHolder.textViewFullName.setText(coin.getFullName());
        coinViewHolder.textViewName.setText(coin.getShortName());
        coinViewHolder.textViewPrice.setText(coin.getPriceStr());
        Picasso.with(mContext).load(coin.getImageURL()).into(coinViewHolder.imageViewIcon);
        if (coin.isFavorite())
            coinViewHolder.isFavoriteImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_favorite_star));
        else
            coinViewHolder.isFavoriteImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_not_favorite_star));

        if (i > mData.size() - 20
                && !isLoading
                && mOnEndReachListener != null) {
            mOnEndReachListener.onEndReach();
            isLoading = true;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnStarClickListener {
        void onStarClick(CoinInfo coinInfo);
    }

    public interface OnEndReachListener {
        void onEndReach();
    }

    void setData(List<CoinInfo> data) {
        mData = data;
        notifyDataSetChanged();
        isLoading = false;
    }

    class CoinViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPrice;
        private TextView textViewFullName;
        private TextView textViewName;
        private ImageView imageViewIcon;
        private ImageView isFavoriteImage;

        CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFullName = itemView.findViewById(R.id.rv_coin_layout_fullname);
            textViewName = itemView.findViewById(R.id.rv_coin_layout_name);
            textViewPrice = itemView.findViewById(R.id.rv_coin_layout_price);
            imageViewIcon = itemView.findViewById(R.id.rv_coin_layout_icon);
            isFavoriteImage = itemView.findViewById(R.id.rv_coin_favorite_image);
            isFavoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnStarClickListener != null)
                        mOnStarClickListener.onStarClick(mData.get(getAdapterPosition()));
                }
            });
        }
    }
}
