package com.example.cryptomonitor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.cryptomonitor.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    public static final String THEME = "theme";
    public static final String LANG = "lang";
    public static final String DEFAULT = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean mTheme = mPreferences.getBoolean(THEME, false);
        if (mTheme.equals(true)) {
            setTheme(R.style.AppThemeDarkPurple);
        } else {
            setTheme(R.style.AppThemeDark);
        }
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String mLang = mPreferences.getString(LANG, DEFAULT);
        Locale mLocale = new Locale(mLang);
        Configuration configuration = new Configuration();
        configuration.locale = mLocale;
        getBaseContext().getResources().updateConfiguration(configuration, null);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        SettingsActivity.this.startActivity(intent);
        SettingsActivity.this.finishAffinity();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference themePreference = findPreference("theme");

            themePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finishAffinity();
                    return true;
                }
            });
            Preference langPreference = findPreference("lang");

            langPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finishAffinity();
                    return true;
                }
            });
        }
    }

}
