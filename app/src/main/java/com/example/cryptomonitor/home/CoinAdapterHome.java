package com.example.cryptomonitor.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.dao.CoinInfoDao;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CoinAdapterHome extends RecyclerView.Adapter<CoinAdapterHome.CoinViewHolder> {


    private Context mContext;
    private List<CoinInfo> mData;
    private OnStarClickListener mOnStarClickListener;


    CoinAdapterHome(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
        onCoinClickListener = (OnCoinClickListener) mContext;

    }


    void setup(Fragment fragment) {
        this.mOnStarClickListener = (OnStarClickListener) fragment;
        this.mOnCoinClickListener = (OnCoinClickListener) fragment;
        mDao = App.getDatabase().coinInfoDao();
        showMode();
    }

    void showMode() {
        if (disposable != null)
            disposable.dispose();
        disposable = mDao.getAllBefore(initialSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mListConsumer);
    }

    void setList(List<CoinInfo> coinInfoList) {
        if (disposable != null)
            disposable.dispose();
        mData = coinInfoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_coin_layout, viewGroup, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder coinViewHolder, int index) {
        if (index + loadSize > mData.size() && !isLoading) {
            loadMore();
        }
        CoinInfo coin = mData.get(index);
        coinViewHolder.textViewFullName.setText(coin.getFullName());
        coinViewHolder.textViewName.setText(coin.getShortName());
        coinViewHolder.textViewPrice.setText(coin.getPriceStr());
        Picasso.with(mContext).load(coin.getImageURL())
                .transform(new PicassoCircleTransformation())
                .into(coinViewHolder.imageViewIcon);

        if (coin.isFavorite())
            coinViewHolder.isFavoriteImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_favorite_star));
        else
            coinViewHolder.isFavoriteImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_not_favorite_star_light));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    void setData(List<CoinInfo> data) {
        mData = data;
        notifyDataSetChanged();
        isLoading = false;
    }

    public interface OnStarClickListener {
        void onStarClick(CoinInfo coinInfo);
    }

    private void loadMore() {
        isLoading = true;
        if (disposable != null)
            disposable.dispose();
        disposable = mDao.getAllBefore(getItemCount() + loadSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mListConsumer);
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
                if (getAdapterPosition() >= 0) {
                    CoinInfo clickedCoin = mData.get(getAdapterPosition());
                    if (clickedCoin.isFavorite())
                        clickedCoin.setFavorite(false);
                    else
                        clickedCoin.setFavorite(true);
                    notifyItemChanged(getAdapterPosition());
                    mOnStarClickListener.onStarClick(clickedCoin);
                }
            });
            itemView.setOnClickListener(v ->
                    mOnCoinClickListener.onCoinClick(mData.get(getAdapterPosition()).getShortName(), getAdapterPosition()));
        }

    }
}

    public interface OnCoinClickListener {
        void onCoinClick(String index, int position);
    }

