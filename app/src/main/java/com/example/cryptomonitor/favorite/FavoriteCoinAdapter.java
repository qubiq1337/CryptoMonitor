package com.example.cryptomonitor.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.coins.CoinInfo;
import com.example.cryptomonitor.home.CoinAdapterHome;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoriteCoinAdapter extends RecyclerView.Adapter<FavoriteCoinAdapter.CoinViewHolder> {
    private OnCoinClickListener mOnCoinClickListener;
    private Context mContext;
    private List<CoinInfo> mData;
    private OnStarClickListener onStarClickListener;

    FavoriteCoinAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
    }


    void setup(Fragment fragment) {
        this.onStarClickListener = (OnStarClickListener) fragment;
        this.mOnCoinClickListener = (OnCoinClickListener) fragment;
    }

    @NonNull
    @Override
    public FavoriteCoinAdapter.CoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_coin_layout, viewGroup, false);
        return new FavoriteCoinAdapter.CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteCoinAdapter.CoinViewHolder coinViewHolder, int i) {
        CoinInfo coin = mData.get(i);
        coinViewHolder.textViewFullName.setText(coin.getFullName());
        coinViewHolder.textViewName.setText(coin.getShortName());
        coinViewHolder.textViewPrice.setText(coin.getPriceDisplay());
        Picasso.get()
                .load(coin.getImageURL())
                .transform(new CoinAdapterHome.PicassoCircleTransformation())
                .into(coinViewHolder.imageViewIcon);
        if (coin.isFavorite())
            coinViewHolder.isFavoriteImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_star));
        else
            coinViewHolder.isFavoriteImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_star_outline));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void setData(List<CoinInfo> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public interface OnStarClickListener {
        void onStarClick(CoinInfo coinInfo);
    }

    public interface OnCoinClickListener {
        void onCoinClick(String index, int position);
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
            isFavoriteImage.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position >= 0) {
                    CoinInfo coinInfo = mData.get(position);
                    coinInfo.setFavorite(false);
                    notifyItemRemoved(position);
                    onStarClickListener.onStarClick(mData.get(position));
                }
            });
            itemView.setOnClickListener(v ->
                    mOnCoinClickListener.onCoinClick(mData.get(getAdapterPosition()).getShortName(), getAdapterPosition()));
        }
    }
}
