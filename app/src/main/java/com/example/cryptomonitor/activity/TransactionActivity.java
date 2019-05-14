package com.example.cryptomonitor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cryptomonitor.AutoCompleteAdapter;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.Purchase;
import com.jakewharton.rxbinding2.widget.RxTextView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.cryptomonitor.Utilities.round;
import static com.example.cryptomonitor.briefcase.BriefcaseFragment.COIN_INDEX;

public class TransactionActivity extends AppCompatActivity {

    private Disposable disposable;
    private AutoCompleteAdapter autoCompleteAdapter;
    private long coinId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        EditText editPrice = findViewById(R.id.transaction_edit_price);
        EditText editAmount = findViewById(R.id.transaction_edit_amount);
        TextView totalCost = findViewById(R.id.transaction_total_cost);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.transaction_actv);
        autoCompleteAdapter = new AutoCompleteAdapter(this);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            long l = 0;
            coinId = intent.getLongExtra(COIN_INDEX,l);
            Log.e("CoinID",coinId+"");
        }


        //умножение Цены на колличество
        disposable = Observable.combineLatest(
                RxTextView
                        .textChangeEvents(editPrice)
                        .skipInitialValue()
                        .map(textChangeEvent -> textChangeEvent.text().toString())
                        .filter(s -> !s.isEmpty())
                        .map(Double::valueOf),
                RxTextView
                        .textChangeEvents(editAmount)
                        .skipInitialValue()
                        .map(textChangeEvent -> textChangeEvent.text().toString())
                        .filter(s -> !s.isEmpty())
                        .map(Double::valueOf),
                (x, y) -> x * y)
                .subscribe(d -> {
                    totalCost.setText(formatTotalCost(d));
                });
    }
    //Форматирование текста в Тотал
    private String formatTotalCost(Double d) {
        String result;
        if (d < 1) {
            BigDecimal bigDecimal = BigDecimal.valueOf(round(d, 2));
            result = bigDecimal.toPlainString();
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
            decimalFormat.setRoundingMode(RoundingMode.CEILING);
            decimalFormat.format(d);
            result = decimalFormat.format(d);
        }
        return "$ " + result;
    }

    @Override
    protected void onDestroy() {
        if (!autoCompleteAdapter.getDisposable().isDisposed())autoCompleteAdapter.getDisposable().dispose();
        if (!disposable.isDisposed()) disposable.dispose();
        autoCompleteAdapter = null;
        super.onDestroy();
    }
}


