package com.example.cryptomonitor.buy;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.Toast;

import com.example.cryptomonitor.R;
import com.example.cryptomonitor.adapters.MinCoinAdapter;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.database.entities.Purchase;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.FinishEvent;
import com.example.cryptomonitor.events.Message;
import com.example.cryptomonitor.fragment.DatePickerFragment;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

public class BuyActivity extends AppCompatActivity implements MinCoinAdapter.OnItemClickListener,
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private static final String SYMBOL_KEY = "symbolKey";
    private static final String COIN_ID_KEY = "coinIdKey";
    private static final String DAY_KEY = "dayKey";
    private static final String MONTH_KEY = "monthKey";
    private static final String YEAR_KEY = "yearKey";

    private RecyclerView mSearchRv;
    private ImageButton mCloseButton;
    private ImageButton mReadyButton;
    private ImageButton mCoinCancelButton;
    private EditText mPriceEdit;
    private EditText mAmountEdit;
    private EditText mNameEdit;
    private TextView mDateEdit;
    private TextView mSelectedCoinTv;
    private ImageView mSelectedCoinIcon;
    private LinearLayout mCoinHolder;
    private TextView mSymbolText;
    private MinCoinAdapter mAdapter;
    private BuyViewModel mViewModel;
    private Purchase mPurchase = new Purchase();

    private long mSelectedCoinId = -1;
    private String mSelectedCoinFullName;
    private String mSelectedCoinIndex;
    private String mSelectedCoinPriceDisplay;
    private double mSelectedCoinPrice;
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
                mSearchViewModel.onTextChanged(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //ignored
        }
    };

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
        mCoinHolder = findViewById(R.id.coin_holder);
        mCoinCancelButton = findViewById(R.id.item_cancel_coin);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.min_coin_item, mCoinHolder, true);
        mSelectedCoinIcon = layout.findViewById(R.id.item_icon);
        mSelectedCoinTv = layout.findViewById(R.id.item_name);

        mDateEdit.setOnClickListener(this);
        mReadyButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);
        mSelectedCoinTv.setOnClickListener(this);
        mCoinCancelButton.setOnClickListener(this);

        mNameEdit.addTextChangedListener(searchTextWatcher);

        mAdapter = new MinCoinAdapter(this, this);
        mSearchRv.setAdapter(mAdapter);

        mViewModel = ViewModelProviders.of(this).get(BuyViewModel.class);
        mViewModel.getSearchLiveData().observe(this, observer);
        mViewModel.getDateLiveData().observe(this, dateObserver);
        mViewModel.getSearchEditTextVisible().observe(this, editTextVisibleObserver);
        mViewModel.getSearchRecyclerVisible().observe(this, recyclerVisibleObserver);
        mViewModel.getSelectedCoinVisible().observe(this, coinInfoVisibleObserver);
        mViewModel.getSelectedCoinLiveData().observe(this, coinInfoObserver);
        mViewModel.getToastLiveData().observe(this, toastObserver);
        final Calendar c = Calendar.getInstance();
        mViewModel.onDateSet(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR));
    }

    @Override
    public void OnItemClick(CoinInfo coinInfo) {
        mViewModel.coinSelected(coinInfo);
    }

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //ignored
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mViewModel.onTextChanged(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            //ignored
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_image:
                mViewModel.exit();
                break;
            case R.id.ready_image:
                mViewModel.ready(mPriceEdit.getText().toString(),
                        mAmountEdit.getText().toString());
                break;
            case R.id.date_edit:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Time Picker");
                break;
            case R.id.item_cancel_coin:
                mViewModel.coinCancelled();
        }
    }

    @Override
    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
        mViewModel.onDateSet(dayOfMonth, month, year);
    }

    private Observer<Event> toastObserver = new Observer<Event>() {
        @Override
        public void onChanged(Event event) {
            if (event != null && !event.isHandled()) {
                if (event instanceof Message) {
                    Message message = (Message) event;
                    Toast.makeText(BuyActivity.this, message.getMessageText(), Toast.LENGTH_SHORT).show();
                } else if (event instanceof FinishEvent) {
                    FinishEvent finishEvent = (FinishEvent) event;
                    finishEvent.accepted();
                    finish();
                }
            }
        }
    };

    private Observer<List<CoinInfo>> observer = new Observer<List<CoinInfo>>() {
        @Override
        public void onChanged(@Nullable List<CoinInfo> coinInfoList) {
            mAdapter.setData(coinInfoList);
        }
    };
    private Observer<Boolean> recyclerVisibleObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean visible) {
            if (visible) {
                mSearchRv.setVisibility(View.VISIBLE);
            } else {
                mSearchRv.setVisibility(View.GONE);
            }

        }
    };
    private Observer<Boolean> editTextVisibleObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean visible) {
            if (visible) {
                mNameEdit.setVisibility(View.VISIBLE);
            } else {
                mNameEdit.setVisibility(View.GONE);
                mNameEdit.setText("");
            }

        }
    };
    private Observer<Boolean> coinInfoVisibleObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean visible) {
            if (visible) {
                mCoinHolder.setVisibility(View.VISIBLE);
                mCoinCancelButton.setVisibility(View.VISIBLE);
            } else {
                mCoinHolder.setVisibility(View.GONE);
                mCoinCancelButton.setVisibility(View.GONE);
            }

        }
    };
    private Observer<String> dateObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String date) {
            mDateEdit.setText(date);
        }
    };
    private Observer<CoinInfo> coinInfoObserver = new Observer<CoinInfo>() {
        @Override
        public void onChanged(CoinInfo coinInfo) {
            if (coinInfo != null) {
                mSelectedCoinTv.setText(coinInfo.getFullName());
                Picasso.with(BuyActivity.this).load(coinInfo.getImageURL()).into(mSelectedCoinIcon);
            }
        }
    };


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
        mViewModel = null;
        super.onDestroy();
    }
}
