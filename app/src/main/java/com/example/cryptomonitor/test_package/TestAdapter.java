package com.example.cryptomonitor.test_package;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {
    private Context mContext;

    public void setCoinInfoList(List<CoinInfo> coinInfoList) {
        this.coinInfoList = coinInfoList;
    }

    private List<CoinInfo> coinInfoList;

    public TestAdapter(Context context){
        mContext = context;
        coinInfoList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.autocomplete_tv_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(coinInfoList.get(i).getFullName());
        Picasso.with(mContext).load(coinInfoList.get(i).getImageURL()).into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return coinInfoList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.autocomplete_item_full_name);
            imageView = itemView.findViewById(R.id.autocomplete_item_icon);
        }
    }
}
