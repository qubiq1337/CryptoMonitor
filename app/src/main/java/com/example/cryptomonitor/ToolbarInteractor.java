package com.example.cryptomonitor;

import android.support.v7.widget.SearchView;
import android.view.View;

public interface ToolbarInteractor extends SearchView.OnQueryTextListener, View.OnClickListener, SearchView.OnCloseListener {
    void setCurrency(String currency);
}
