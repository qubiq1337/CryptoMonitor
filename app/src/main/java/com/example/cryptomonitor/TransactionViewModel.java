package com.example.cryptomonitor;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptomonitor.database.bills.BillDataSource;
import com.example.cryptomonitor.database.bills.BillRepo;
import com.example.cryptomonitor.database.entities.Bill;
import com.example.cryptomonitor.database.entities.CoinInfo;
import com.example.cryptomonitor.database.entities.Purchase;
import com.example.cryptomonitor.database.purchases.PurchaseAndCoin;
import com.example.cryptomonitor.database.purchases.PurchaseDataSource;
import com.example.cryptomonitor.database.purchases.PurchaseRepo;
import com.example.cryptomonitor.events.Event;
import com.example.cryptomonitor.events.FinishEvent;
import com.example.cryptomonitor.events.Message;
import com.example.cryptomonitor.events.PriceEvent;
import com.example.cryptomonitor.model_cryptocompare.model_currencies.CurrenciesData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.disposables.Disposable;

import static com.example.cryptomonitor.Utilities.dateFormatting;
import static com.example.cryptomonitor.Utilities.simpleNumberFormatting;
import static com.example.cryptomonitor.activity.TransactionActivity.EDIT_MODE;
import static com.example.cryptomonitor.activity.TransactionActivity.SELL_MODE;


public class TransactionViewModel extends ViewModel {
    private MutableLiveData<CoinInfo> mSelectedCoin = new MutableLiveData<>();
    private CoinInfo mCurrentCoinInfo;
    private PurchaseAndCoin mCurrentPurchaseAndCoin;
    private Purchase mPurchase;
    private MutableLiveData<String> mDateSet = new MutableLiveData<>();
    private MutableLiveData<Event> mPriceEventLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mAmountLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> mCoinCanceledVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> mAutoCompleteTextViewEnabled = new MutableLiveData<>();
    private MutableLiveData<String> mAutoCompleteTextLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mReadyButtonName = new MutableLiveData<>();
    private MutableLiveData<Event> mEvent = new MutableLiveData<>();
    private PurchaseDataSource mPurchaseDataSource = new PurchaseRepo();
    private MutableLiveData<Boolean> mBuyButtonCheck = new MutableLiveData<>();
    private MutableLiveData<String> mSymbolLiveData = new MutableLiveData<>();
    private Disposable disposable;
    private String MODE = "BUY MODE";
    private CurrenciesData mCurrencies;
    private BillDataSource billDataSource = new BillRepo();
    private Bill mBill;
    private String nowDate = "";
    private String buyDatePurchase = "";
    public static final HashMap<String, String> coinSymbols = new HashMap<>();

    static {
        coinSymbols.put("\u0024", "USD");
        coinSymbols.put("\u20AC", "EUR");
        coinSymbols.put("\u20BD", "RUB");
        coinSymbols.put("\u5713", "CNY");
        coinSymbols.put("\uFFE1", "GBP");
    }

    public LiveData<String> getSymbolLiveData() {
        return mSymbolLiveData;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public LiveData<String> getAmountLiveData() {
        return mAmountLiveData;
    }

    public LiveData<Boolean> getBuyButtonCheck() {
        return mBuyButtonCheck;
    }

    public LiveData<String> getReadyButtonName() {
        return mReadyButtonName;
    }

    public LiveData<String> getAutoCompleteTextLiveData() {
        return mAutoCompleteTextLiveData;
    }

    public LiveData<String> getDateLiveData() {
        return mDateSet;
    }

    public LiveData<Event> getToastLiveData() {
        return mEvent;
    }

    public LiveData<Event> getPriceEventLiveData() {
        return mPriceEventLiveData;
    }

    public LiveData<Integer> getCoinCanceledVisible() {
        return mCoinCanceledVisible;
    }

    public LiveData<Boolean> getAutoCompleteTextViewEnabled() {
        return mAutoCompleteTextViewEnabled;
    }

    public TransactionViewModel() {
        defaultSetup();
    }


    public void coinSelected(CoinInfo coinInfo) {
        mCurrentCoinInfo = coinInfo;
        mSelectedCoin.setValue(coinInfo);
        String price = simpleNumberFormatting(coinInfo.getPrice());
        String symbol = coinInfo.getSymbol();
        mPriceEventLiveData.setValue(new PriceEvent(symbol, price));
        mCoinCanceledVisible.setValue(View.VISIBLE);
        mAutoCompleteTextViewEnabled.setValue(false);
    }

    public void coinCancelled() {
        mCurrentCoinInfo = null;
        mCoinCanceledVisible.setValue(View.GONE);
        mAutoCompleteTextViewEnabled.setValue(true);

    }

    public void onDateSet(int day, int month, int year) {
        mPurchase.setDay(day);
        mPurchase.setMonth(month + 1);
        mPurchase.setYear(year);
        if (nowDate.isEmpty()) nowDate = mPurchase.getDateStr();
        mBill.setSell_date(mPurchase.getDateStr());
        mDateSet.setValue(dateFormatting(mPurchase.getDateStr()));
    }

    public void ready(String priceStr, String amountStr) {
        double price;
        double amount;
        if (priceStr.isEmpty() || amountStr.isEmpty())
            mEvent.setValue(new Message("Fill empty fields"));
        else if (Double.parseDouble(priceStr) == 0 || Double.parseDouble(amountStr) == 0)
            mEvent.setValue(new Message("Fields can`t be 0"));
        else
            try {
                price = Double.parseDouble(priceStr);
                amount = Double.parseDouble(amountStr);

                switch (MODE) {
                    case (EDIT_MODE):
                        mPurchase.setAmount(amount);
                        mPurchase.setPrice_purchase(price);
                        mPurchaseDataSource.update(mPurchase);
                        mEvent.setValue(new FinishEvent());
                        break;
                    case (SELL_MODE):
                        mBill.setAmount(amount);
                        mBill.setSellPrice(price);
                        mBill.setBuyPrice(mPurchase.getPrice_purchase());
                        mBill.setFull_name(mCurrentPurchaseAndCoin.getCoinFullName());
                        mBill.setShort_name(mCurrentPurchaseAndCoin.getCoinIndex());
                        mBill.setBuy_date(buyDatePurchase);
                        mBill.setBuy_currency_symbol(mPurchase.getBuyCurrencySymbol());
                        mBill.setImage_url(mCurrentPurchaseAndCoin.getIconURL());

                        double purchaseAmount = mPurchase.getAmount();
                        if (amount < purchaseAmount) {
                            billDataSource.insert(mBill);
                            mPurchase.setAmount(purchaseAmount - amount);
                            mEvent.setValue(new FinishEvent());
                            mPurchaseDataSource.update(mPurchase);
                        } else if (amount == purchaseAmount) {
                            billDataSource.insert(mBill);
                            mEvent.setValue(new FinishEvent());
                            mPurchaseDataSource.remove(mPurchase);
                        } else mEvent.setValue(new Message("You can`t sell more then you have"));
                        break;
                    default:
                        mPurchase.setAmount(amount);
                        mPurchase.setPrice_purchase(price);
                        //Добавление символа $
                        mPurchase.setBuyCurrencySymbol(mCurrentCoinInfo.getSymbol());
                        // Добавления валюты USD
                        mPurchase.setBuyCurrency(coinSymbols.get(mCurrentCoinInfo.getSymbol()));
                        mPurchase.setCoinId(mCurrentCoinInfo.getId());
                        mPurchaseDataSource.insert(mPurchase);
                        mEvent.setValue(new FinishEvent());
                        break;
                }
            } catch (NullPointerException e) {
                mEvent.setValue(new Message("Select coin"));
            } catch (NumberFormatException e) {
                mEvent.setValue(new Message("Wrong format"));
            }
    }

    // дефолтное состояние при Buy режиме
    private void defaultSetup() {
        mBill = new Bill();
        mPurchase = new Purchase();
        mCurrentPurchaseAndCoin = null;
        mAutoCompleteTextLiveData.setValue("");
        mReadyButtonName.setValue("Buy");
        mCurrentCoinInfo = null;
    }

    public void exit() {
        mEvent.setValue(new FinishEvent());
    }

    //Нужен для загрузки PurchaseAndCoin и Currency
    public void startDefaultEditMode(long id) {
        getCurrentPurchase(id);
    }

    // Стартовое состояние полей для редактирования/продажи
    private void initMode(PurchaseAndCoin purchaseAndCoin) {
        mCurrentPurchaseAndCoin = purchaseAndCoin;
        mPurchase = mCurrentPurchaseAndCoin.getPurchase();
        //Сохраняем дату покупки Purchase
        buyDatePurchase = mPurchase.getDateStr();
        mAutoCompleteTextViewEnabled.setValue(false);
        mAutoCompleteTextLiveData.setValue(mCurrentPurchaseAndCoin.getCoinFullName());
        mSymbolLiveData.setValue(mCurrentPurchaseAndCoin.getPurchase().getBuyCurrencySymbol());
        //После чека Buy переключается на режим редактирования (onCheckedChanged)
        mBuyButtonCheck.setValue(true);
    }

    // Смена режима редактирвания и продажи
    public void changeMode(String mode) {
        switch (mode) {
            case EDIT_MODE:
                initEditMode();
                break;
            case SELL_MODE:
                initSellMode();
                break;
        }
    }

    // получение PurchaseAndCoin по выбранной монете надо объединить тк если getPurchase(id) выполнится быстрее то в initMode придет mCurrencies = null
    private void getCurrentPurchase(long id) {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
        disposable = mPurchaseDataSource
                .getPurchase(id)
                .filter(purchaseAndCoins -> !purchaseAndCoins.isEmpty())
                .map(purchaseAndCoins -> purchaseAndCoins.get(0))
                .subscribe(this::initMode,
                        e -> mEvent.setValue(new Message("Failed load coin"))
                );

        mPurchaseDataSource.getCurrencyData(new PurchaseDataSource.GetCurrenciesCallBack() {
            @Override
            public void onLoaded(CurrenciesData currenciesData) {
                mCurrencies = currenciesData;
            }

            @Override
            public void onFailed() {

            }
        });
    }

    //Если EditMode = On
    private void initEditMode() {
        MODE = EDIT_MODE;
        String price = simpleNumberFormatting(mPurchase.getPrice_purchase());
        String symbol = mPurchase.getBuyCurrencySymbol();
        mPriceEventLiveData.setValue(new PriceEvent(symbol, price));
        mAmountLiveData.setValue(simpleNumberFormatting(mCurrentPurchaseAndCoin.getPurchase().getAmount()));
        mDateSet.setValue(dateFormatting(buyDatePurchase));
        mReadyButtonName.setValue("Update");
    }

    //Если SellMode = On
    private void initSellMode() {
        Log.e("initSellMode", "initSellMode");
        MODE = SELL_MODE;
        // Символ валюты в которой была произведена покупка $...
        String buyCurrencySymbol = mCurrentPurchaseAndCoin.getPurchase().getBuyCurrencySymbol();
        // Название валюты в которой была произведена покупка USD...
        String buyCurrency = mCurrentPurchaseAndCoin.getPurchase().getBuyCurrency();
        // Текущая цена монеты в выбранной в настройках валюте
        Double currentPrice = mCurrentPurchaseAndCoin.getDouble_price();
        // Конвертирование текущей цены монеты в валюту в которой происходила покупка
        Double convertedCurrentPrice = convert(currentPrice, getBuyCurrencyPrice(buyCurrency));

        mPriceEventLiveData.setValue(new PriceEvent(buyCurrencySymbol, simpleNumberFormatting(convertedCurrentPrice)));
        mAmountLiveData.setValue(simpleNumberFormatting(mCurrentPurchaseAndCoin.getPurchase().getAmount()));
        //Дата now
        mDateSet.setValue(dateFormatting(nowDate));

        mReadyButtonName.setValue("Sell");
    }

    private Double convert(Double currentPrice, Double buyCurrencyPrice) {
        if (buyCurrencyPrice == null) {
            return 1D;
        } else {
            return currentPrice * buyCurrencyPrice;
        }
    }

    // Возвращает buyCurrencyPrice текущую цену валюты в которой присходила покупка
    private Double getBuyCurrencyPrice(String buyCurrency) {
        switch (buyCurrency) {
            case ("USD"):
                return mCurrencies.getUSD();
            case ("EUR"):
                return mCurrencies.getEUR();
            case ("CNY"):
                return mCurrencies.getCNY();
            case ("RUB"):
                return mCurrencies.getRUB();
            case ("GBP"):
                return mCurrencies.getGBP();
        }
        return 1D;
    }

    private int[] strToDate(String dateStr) {
        int[] date = new int[3];
        SimpleDateFormat dayf = new SimpleDateFormat("dd", Locale.US);
        SimpleDateFormat monf = new SimpleDateFormat("MM", Locale.US);
        SimpleDateFormat yearf = new SimpleDateFormat("YYYY", Locale.US);
        date[0]=Integer.parseInt(dayf.format(dateStr));
        date[1] = Integer.parseInt(monf.format(dateStr));
        date[2] = Integer.parseInt(yearf.format(dateStr));
        return date;
    }

}
