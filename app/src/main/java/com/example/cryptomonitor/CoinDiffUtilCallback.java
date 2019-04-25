package com.example.cryptomonitor;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.example.cryptomonitor.database.entities.CoinInfo;

public class CoinDiffUtilCallback extends DiffUtil.ItemCallback<CoinInfo> {

    @Override
    public boolean areItemsTheSame(@NonNull CoinInfo oldInfo, @NonNull CoinInfo newInfo) {
        return oldInfo.getId() == newInfo.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull CoinInfo oldInfo, @NonNull CoinInfo newInfo) {
        return TextUtils.equals(oldInfo.getFullName(), newInfo.getFullName())
                && TextUtils.equals(oldInfo.getImageURL(), newInfo.getImageURL())
                && TextUtils.equals(oldInfo.getShortName(), newInfo.getShortName())
                && TextUtils.equals(oldInfo.getPriceStr(), newInfo.getPriceStr())
                && oldInfo.isFavorite() == newInfo.isFavorite();
    }
}
