package com.example.cryptomonitor.test_package;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.jakewharton.rxbinding2.widget.RxSearchView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {
    private TestAdapter testAdapter;
    private List<CoinInfo> blankList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        EditText editText = findViewById(R.id.test_edit_text);
        RecyclerView recyclerView = findViewById(R.id.test_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        testAdapter = new TestAdapter(this);
        recyclerView.setAdapter(testAdapter);
        /*SearchView searchView = findViewById(R.id.test_search);*/
        LinearLayout layoutOn = findViewById(R.id.test_visible_layout);
        LinearLayout layoutSearch = findViewById(R.id.test_gone_layout);
        ImageView searchOn = findViewById(R.id.test_search_button_visible);
        searchOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOn.setVisibility(View.GONE);
                layoutSearch.setVisibility(View.VISIBLE);
            }
        });

        ImageView searchOff = findViewById(R.id.test_search_button_gone);
        searchOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOn.setVisibility(View.VISIBLE);
                layoutSearch.setVisibility(View.GONE);
            }
        });
        Disposable disposable = RxTextView
                .textChangeEvents(editText)
                .skipInitialValue()
                .map(TextViewTextChangeEvent::text)
                .map(CharSequence::toString)
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> { if (s.isEmpty()) refreshAdapter(blankList);})
                .filter(s -> !s.isEmpty())
                .switchMap(s -> App
                        .getDatabase()
                        .coinInfoDao()
                        .getSearchCoins(s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(subscription -> Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show())
                        .doOnTerminate(() -> Log.e("Tag", "Finish"))
                        .toObservable())
                .subscribe(this::refreshAdapter);
    }

    private void refreshAdapter(List<CoinInfo> coinInfoList) {
        testAdapter.setCoinInfoList(coinInfoList);
        testAdapter.notifyDataSetChanged();
    }
}
