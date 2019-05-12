package com.example.cryptomonitor.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.cryptomonitor.AutoCompleteAdapter;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.App;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.transaction_actv);
        AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(this);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setClickable(true);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> autoCompleteTextView.setEnabled(false));

    }
}
