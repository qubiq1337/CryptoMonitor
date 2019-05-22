package com.example.cryptomonitor.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cryptomonitor.HistoryViewModel;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.TransactionViewModel;
import com.example.cryptomonitor.adapters.HistoryAdapter;
import com.example.cryptomonitor.database.entities.Bill;

import java.util.List;


public class HistoryFragment extends Fragment {

    private HistoryAdapter mHistoryAdapter;
    private HistoryViewModel mHistoryViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.history_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHistoryAdapter = new HistoryAdapter(getActivity());
        mRecyclerView.setAdapter(mHistoryAdapter);
        mHistoryViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        mHistoryViewModel.getBillsLiveData().observe(this,billsLiveDataObserver);
        return view;
    }

    private Observer<List<Bill>> billsLiveDataObserver = new Observer<List<Bill>>() {
        @Override
        public void onChanged(List<Bill> bills) {
            mHistoryAdapter.setBillList(bills);
        }
    };

}
