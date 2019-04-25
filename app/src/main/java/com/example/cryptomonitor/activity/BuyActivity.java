package com.example.cryptomonitor.activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cryptomonitor.AppExecutors;
import com.example.cryptomonitor.CoinDiffUtilCallback;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.adapters.MinCoinAdapter;
import com.example.cryptomonitor.database.App;
import com.example.cryptomonitor.database.PurchaseDataHelper;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.database.entities.Purchase;
import com.example.cryptomonitor.fragment.DatePickerFragment;
import com.example.cryptomonitor.view_models.SearchViewModel;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class BuyActivity extends AppCompatActivity implements MinCoinAdapter.OnItemClickListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String SYMBOL_KEY = "symbolKey";
    private static final String COIN_ID_KEY = "coinIdKey";
    private static final String DAY_KEY = "dayKey";
    private static final String MONTH_KEY = "monthKey";
    private static final String YEAR_KEY = "yearKey";

    private RecyclerView mSearchRv;
    private ImageButton mCloseButton;
    private ImageButton mReadyButton;
    private EditText mPriceEdit;
    private EditText mAmountEdit;
    private EditText mNameEdit;
    private TextView mDateEdit;
    private TextView mSelectedCoinTv;
    private ImageView mSelectedCoinIcon;
    private LinearLayout mSelectedCoinContainer;
    private TextView mSymbolText;
    private MinCoinAdapter mAdapter;
    private SearchViewModel mSearchViewModel;
    private Purchase mPurchase = new Purchase();
    private Observer<PagedList<CoinInfo>> observer = new Observer<PagedList<CoinInfo>>() {
        @Override
        public void onChanged(@Nullable PagedList<CoinInfo> coinInfoList) {
            mAdapter.submitList(coinInfoList);
        }
    };
    private long mSelectedCoinId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        mNameEdit = findViewById(R.id.name_edit);
        mAmountEdit = findViewById(R.id.amount_edit);
        mPriceEdit = findViewById(R.id.price_edit);
        mSearchRv = findViewById(R.id.search_rv);
        mCloseButton = findViewById(R.id.close_image);
        mReadyButton = findViewById(R.id.ready_image);
        mDateEdit = findViewById(R.id.date_edit);
        mSymbolText = findViewById(R.id.symbol_tv);
        mSelectedCoinContainer = LayoutInflater.inflate(resource, mSelectedCoinContainer, false);

        mDateEdit.setOnClickListener(this);
        mReadyButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);
        mSelectedCoinTv.setOnClickListener(this);

        mNameEdit.addTextChangedListener(searchTextWatcher);

        mAdapter = new MinCoinAdapter(new CoinDiffUtilCallback(), this, this);
        mSearchRv.setAdapter(mAdapter);

        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mSearchViewModel.getSearchLiveData().observe(this, observer);
        final Calendar c = Calendar.getInstance();
        onDateChanged(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void OnItemClick(CoinInfo coinInfo) {
        mSearchRv.setVisibility(View.GONE);
        mNameEdit.setVisibility(View.GONE);
        mSelectedCoinTv.setVisibility(View.VISIBLE);
        mSelectedCoinId = coinInfo.getId();
        mSelectedCoinTv.setText(coinInfo.getFullName());
        Picasso.with(this).load(coinInfo.getImageURL()).into(mSelectedCoinTv);
        mPriceEdit.setText(String.valueOf(coinInfo.getPrice()));
        mSymbolText.setText(coinInfo.getSymbol());
    }

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //ignored
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().isEmpty())
                mSearchRv.setVisibility(View.GONE);
            else {
                mSearchRv.setVisibility(View.VISIBLE);
                mSearchViewModel.getSearchLiveData().removeObservers(BuyActivity.this);
                mSearchViewModel.getSearchLiveData(s.toString()).observe(BuyActivity.this, observer);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //ignored
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selected_item:
                mSelectedCoinTv.setVisibility(View.GONE);
                mNameEdit.setVisibility(View.VISIBLE);
                mNameEdit.setText("");
                mSelectedCoinId = -1;
                break;
            case R.id.close_image:
                finish();
                break;
            case R.id.ready_image:
                if (fieldsAreValid()) {
                    mPurchase.setAmount(Double.valueOf(mAmountEdit.getText().toString()));
                    mPurchase.setCoinId(mSelectedCoinId);
                    PurchaseDataHelper.insert(mPurchase);
                    finish();
                }
                break;
            case R.id.date_edit:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Time Picker");
        }
    }

    boolean fieldsAreValid() {
        return mSelectedCoinId != -1
                && !mAmountEdit.getText().toString().isEmpty()
                && !mPriceEdit.getText().toString().isEmpty();
    }

    @Override
    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
        onDateChanged(year, month, dayOfMonth);
    }

    private void onDateChanged(int year, int month, int dayOfMonth) {
        mPurchase.setDay(dayOfMonth);
        mPurchase.setMonth(month + 1);
        mPurchase.setYear(year);
        mDateEdit.setText(mPurchase.getDateStr());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        final long coinId = savedInstanceState.getLong(COIN_ID_KEY);
        AppExecutors.getInstance().getDbExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final CoinInfo coinInfo = App.getDatabase().coinInfoDao().getById(coinId);
                if (coinInfo != null)
                    mSelectedCoinTv.post(new Runnable() {
                        @Override
                        public void run() {
                            mSearchRv.setVisibility(View.GONE);
                            mNameEdit.setVisibility(View.GONE);
                            mSelectedCoinTv.setVisibility(View.VISIBLE);
                            mSelectedCoinId = coinInfo.getId();
                            mSelectedCoinTv.setText(coinInfo.getFullName());
                            mPriceEdit.setText(String.valueOf(coinInfo.getPrice()));
                            mSymbolText.setText(coinInfo.getSymbol());
                        }
                    });
            }
        });
        int day = savedInstanceState.getInt(DAY_KEY);
        int month = savedInstanceState.getInt(MONTH_KEY);
        int year = savedInstanceState.getInt(YEAR_KEY);
        onDateChanged(day, month, year);
        mSymbolText.setText(savedInstanceState.getString(SYMBOL_KEY));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SYMBOL_KEY, mSymbolText.getText().toString());
        outState.putLong(COIN_ID_KEY, mSelectedCoinId);
        outState.putInt(DAY_KEY, mPurchase.getDay());
        outState.putInt(MONTH_KEY, mPurchase.getMonth() - 1);
        outState.putInt(YEAR_KEY, mPurchase.getYear());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mSearchRv = null;
        mCloseButton = null;
        mReadyButton = null;
        mPriceEdit = null;
        mAmountEdit = null;
        mNameEdit = null;
        mDateEdit = null;
        mSelectedCoinTv = null;
        mSymbolText = null;
        mAdapter = null;
        mSearchViewModel = null;
        super.onDestroy();
    }
}
