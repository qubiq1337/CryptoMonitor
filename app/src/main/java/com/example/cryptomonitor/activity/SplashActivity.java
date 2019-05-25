package com.example.cryptomonitor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPreferences();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void setPreferences(){
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mCurrency = mPreferences.getString("currency", "default");
        setCurrency(mCurrency);
    }

    public void setCurrency(String mCurrency){
        if (mCurrency.equals("default")){
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("currency", "usd");
            editor.apply();
        }
    }
}
