package com.example.cryptomonitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.ToolbarInteractor;
import com.example.cryptomonitor.activity.BuyActivity;
import com.example.cryptomonitor.activity.MainActivity;


public class BriefcaseFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton mPlusButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_briefcase, container, false);
        mPlusButton = view.findViewById(R.id.floatingActionButton);
        mPlusButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButton:
                Intent intent = new Intent(this.getContext(), BuyActivity.class);
                startActivity(intent);
                break;
        }
    }
}
