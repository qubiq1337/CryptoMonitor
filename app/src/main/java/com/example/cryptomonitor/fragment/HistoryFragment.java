package com.example.cryptomonitor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptomonitor.HistoryViewModel;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.adapters.HistoryAdapter;
import com.example.cryptomonitor.database.entities.Bill;

import java.util.List;


public class HistoryFragment extends Fragment {

    private HistoryAdapter mHistoryAdapter;
    private HistoryViewModel mHistoryViewModel;
    private Observer<List<Bill>> billsLiveDataObserver = new Observer<List<Bill>>() {
        @Override
        public void onChanged(List<Bill> bills) {
            mHistoryAdapter.setBillList(bills);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.history_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHistoryAdapter = new HistoryAdapter(getActivity());
        mRecyclerView.setAdapter(mHistoryAdapter);
        mHistoryViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        mHistoryViewModel.getBillsLiveData().observe(this, billsLiveDataObserver);
        return view;
    }

}
