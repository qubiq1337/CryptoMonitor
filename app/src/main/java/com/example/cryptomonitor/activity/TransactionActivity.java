package com.example.cryptomonitor.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import com.example.cryptomonitor.AutoCompleteAdapter;
import com.example.cryptomonitor.R;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.transaction_actv);
        AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(this);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

    }
}
