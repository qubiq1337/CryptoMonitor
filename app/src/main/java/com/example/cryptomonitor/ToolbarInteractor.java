package com.example.cryptomonitor;

import androidx.appcompat.widget.SearchView;
import android.view.View;

public interface ToolbarInteractor extends SearchView.OnQueryTextListener, View.OnClickListener, SearchView.OnCloseListener {
    void setCurrency(String currency);
}
