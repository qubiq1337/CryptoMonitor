package com.example.cryptomonitor.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.CoinInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CoinAdapterHome extends RecyclerView.Adapter<CoinAdapterHome.CoinViewHolder> {

    private static final String URL = "https://www.cryptocompare.com";
    private List<CoinInfo> coinData;
    private Context mContext;
    private OnStarClickListener onStarClickListener;

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    private OnLoadListener onLoadListener;

    public CoinAdapterHome(Context context, RecyclerView recyclerView) {
        mContext = context;
        coinData = new ArrayList<>();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                int zapas = 30;
                int totalSize = linearLayoutManager.getItemCount();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (totalSize == (lastVisibleItemPosition + zapas)) {
                    int page = totalSize;
                    onLoadListener.loadMore(page - 1);
                }
            }
        });
    }

    public List<CoinInfo> getCoinData() {
        return coinData;
    }

    public void setCoinData(List<CoinInfo> coinData) {
        this.coinData = coinData;
        notifyDataSetChanged();
    }

    public void setOnStarClickListener(OnStarClickListener onStarClickListener) {
        this.onStarClickListener = onStarClickListener;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_coin_layout, viewGroup, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder coinViewHolder, int i) {
        coinViewHolder.textViewFullName.setText(coinData.get(i).getFullName());
        coinViewHolder.textViewName.setText(coinData.get(i).getShortName());
        coinViewHolder.textViewPrice.setText(coinData.get(i).getPrice());
        if (coinData.get(i).isFavorite())
            coinViewHolder.isFavoriteImage.setImageDrawable(mContext.getDrawable(R.drawable.is_favorite_star));
        else
            coinViewHolder.isFavoriteImage.setImageDrawable(mContext.getDrawable(R.drawable.not_favorite_star));
        Picasso.with(mContext).load(URL + coinData.get(i).getImageURL()).into(coinViewHolder.imageViewIcon);
    }

    @Override
    public int getItemCount() {
        return coinData.size();
    }

    public interface OnStarClickListener {
        void onStarClick(int position);
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
                    onStarClickListener.onStarClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnLoadListener {
        void loadMore(int page);
    }


}
