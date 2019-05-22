package com.example.cryptomonitor.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cryptomonitor.AutoCompleteAdapter;
import com.example.cryptomonitor.R;
import com.example.cryptomonitor.TransactionViewModel;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.FinishEvent;
import com.example.cryptomonitor.events.Message;
import com.example.cryptomonitor.events.PriceEvent;
import com.example.cryptomonitor.fragment.DatePickerFragment;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.example.cryptomonitor.TransactionViewModel.coinSymbols;
import static com.example.cryptomonitor.Utilities.cashFormatting;
import static com.example.cryptomonitor.briefcase.BriefcaseFragment.COIN_INDEX;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final String EDIT_MODE = "EDIT MODE";
    public static final String SELL_MODE = "SELL MODE";
    private AutoCompleteAdapter autoCompleteAdapter;
    private EditText mEditPrice;
    private EditText mEditAmount;
    private TextView mTotalCost;
    private AutoCompleteTextView mAutoCompleteTextView;
    private TextView mDate;
    private ImageButton mCancelCoin;
    private TransactionViewModel mTransactionViewModel;
    private TextView mPriceIn;
    private RadioGroup mRadioGroup;
    private RadioButton mBuyRadioButton;
    private RadioButton mSellRadioButton;
    private TextView mReadyButton;
    private TextView mCancelButton;
    private long coinId;
    private Disposable disposable;
    private TextView mPriceSymbol;
    private TextView mTotalCostSymbol;
    private TextView mTotalCostText;
    //Вывод сообщений ошибок
    private Observer<Event> toastObserver = event -> {
        if (event != null && !event.isHandled()) {
            if (event instanceof Message) {
                Message message = (Message) event;
                Toast.makeText(TransactionActivity.this, message.getMessageText(), Toast.LENGTH_SHORT).show();
                message.handled();
            } else if (event instanceof FinishEvent) {
                FinishEvent finishEvent = (FinishEvent) event;
                finishEvent.handled();
                finish();
            }
        }
    };
    // Состояние автокомплита после выбора монеты/ отмены
    private Observer<Boolean> autoCompleteTextViewEnabledObserver = enabled -> {
        if (enabled) {
            mAutoCompleteTextView.setEnabled(true);
            mAutoCompleteTextView.getText().clear();
            mAutoCompleteTextView.requestFocus();
            mEditPrice.setText("");
        } else {
            mAutoCompleteTextView.setEnabled(false);
        }
    };
    //  отмена выбранного коина
    private Observer<Integer> cancelCoinVisibleObserver = visibility ->
            mCancelCoin.setVisibility(visibility);
    // Изменение price
    private Observer<Event> priceEventObserver = priceEvent -> {
        if (priceEvent != null && !priceEvent.isHandled()) {
            PriceEvent price = (PriceEvent) priceEvent;
            mEditPrice.setText(price.getPrice());
        }
    };
    //сетим текущую дату в TV
    private Observer<String> dateObserver = date ->
            mDate.setText(date);
    // Изменение текста кнопки Ready
    private Observer<String> readyButtonNameObserver = s ->
            mReadyButton.setText(s);
    //Изменение текста autocomplete
    private Observer<String> autocompleteTextObserver = s ->
            mAutoCompleteTextView.setText(s);
    //Первая инициализация EditMode
    private Observer<Boolean> buyButtonObserver = aBoolean ->
            mBuyRadioButton.setChecked(aBoolean);
    //Изменение Amount
    private Observer<String> amountLiveDataObserver = s ->
            mEditAmount.setText(s);
    //Изменение символа валюты
    private Observer<String> symbolLiveDataObserver = s -> {
        //USD/EUR ...
        String currency = coinSymbols.get(s);
        mPriceIn.setText(getString(R.string.transaction_price_in, currency));
        mTotalCostText.setText(getString(R.string.transaction_total_cost, currency));
        mTotalCostSymbol.setText(s);
        mPriceSymbol.setText(s);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mEditPrice = findViewById(R.id.transaction_edit_price);
        mEditAmount = findViewById(R.id.transaction_edit_amount);
        mTotalCost = findViewById(R.id.transaction_total_cost);
        mAutoCompleteTextView = findViewById(R.id.transaction_actv);
        mDate = findViewById(R.id.transaction_date);
        mCancelCoin = findViewById(R.id.transaction_cancel_selected_coin);
        mPriceIn = findViewById(R.id.transaction_price_in);
        mRadioGroup = findViewById(R.id.transaction_radio_group);
        mBuyRadioButton = findViewById(R.id.transaction_buy_button);
        mSellRadioButton = findViewById(R.id.transaction_sell_button);
        mReadyButton = findViewById(R.id.transaction_ready);
        mCancelButton = findViewById(R.id.transaction_cancel);
        mPriceSymbol = findViewById(R.id.transaction_price_symbol);
        mTotalCostSymbol = findViewById(R.id.transaction_total_cost_symbol);
        mTotalCostText = findViewById(R.id.transaction_total_cost_text);
        mCancelButton.setOnClickListener(this);
        mReadyButton.setOnClickListener(this);

        autoCompleteAdapter = new AutoCompleteAdapter(this);
        mAutoCompleteTextView.setAdapter(autoCompleteAdapter);

        mDate.setOnClickListener(this);
        mCancelCoin.setOnClickListener(this);

        mTransactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        mTransactionViewModel.getCoinCanceledVisible().observe(this, cancelCoinVisibleObserver);
        mTransactionViewModel.getAutoCompleteTextViewEnabled().observe(this, autoCompleteTextViewEnabledObserver);
        mTransactionViewModel.getPriceEventLiveData().observe(this, priceEventObserver);
        mTransactionViewModel.getDateLiveData().observe(this, dateObserver);
        mTransactionViewModel.getToastLiveData().observe(this, toastObserver);
        mTransactionViewModel.getAutoCompleteTextLiveData().observe(this, autocompleteTextObserver);
        mTransactionViewModel.getReadyButtonName().observe(this, readyButtonNameObserver);
        mTransactionViewModel.getBuyButtonCheck().observe(this, buyButtonObserver);
        mTransactionViewModel.getAmountLiveData().observe(this, amountLiveDataObserver);
        mTransactionViewModel.getSymbolLiveData().observe(this, symbolLiveDataObserver);

        final Calendar c = Calendar.getInstance();
        mTransactionViewModel.onDateSet(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR));

        Intent intent = getIntent();
        if (intent != null) {
            long l = 0;
            coinId = intent.getLongExtra(COIN_INDEX, l);
            if (coinId != 0) {
                mTransactionViewModel.startDefaultEditMode(coinId);
            } else {
                mRadioGroup.setVisibility(View.GONE);
            }
        }
        //Клик на элемент списка (Выбор монеты)
        mAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            CoinInfo coinInfo = autoCompleteAdapter.getReusultFilterList().get(position);
            mTransactionViewModel.coinSelected(coinInfo);
        });
        //Слушатель radio group
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case (R.id.transaction_buy_button):
                    mTransactionViewModel.changeMode(EDIT_MODE);
                    break;
                case (R.id.transaction_sell_button):
                    mTransactionViewModel.changeMode(SELL_MODE);
                    break;
            }
        });

        //умножение цены на колличество (TotalCost)
        disposable = Observable.combineLatest(
                RxTextView
                        .textChangeEvents(mEditPrice)
                        .map(textChangeEvent -> textChangeEvent.text().toString())
                        .map(s -> s.isEmpty() ? 0D : Double.parseDouble(s))
                ,
                RxTextView
                        .textChangeEvents(mEditAmount)
                        .map(textChangeEvent -> textChangeEvent.text().toString())
                        .map(s -> s.isEmpty() ? 0D : Double.parseDouble(s))
                ,
                (x, y) -> x * y)
                .subscribe(d -> mTotalCost.setText(cashFormatting(d)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transaction_cancel:
                mTransactionViewModel.exit();
                break;
            case R.id.transaction_ready:
                mTransactionViewModel.ready(mEditPrice.getText().toString(), mEditAmount.getText().toString());
                break;
            case (R.id.transaction_cancel_selected_coin):
                mTransactionViewModel.coinCancelled();
                break;
            case (R.id.transaction_date):
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Time Picker");
                break;
        }
    }

    //сетим текущую дату в VM
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mTransactionViewModel.onDateSet(dayOfMonth, month, year);
    }

    @Override
    protected void onDestroy() {

        if (mTransactionViewModel.getDisposable() != null && !mTransactionViewModel.getDisposable().isDisposed())
            mTransactionViewModel.getDisposable().dispose();

        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();

        autoCompleteAdapter = null;
        mEditPrice = null;
        mEditAmount = null;
        mTotalCost = null;
        mAutoCompleteTextView = null;
        mDate = null;
        mCancelCoin = null;
        mTransactionViewModel = null;
        mPriceIn = null;
        mRadioGroup = null;
        mBuyRadioButton = null;
        mSellRadioButton = null;
        mReadyButton = null;
        mCancelButton = null;
        super.onDestroy();
    }
}
