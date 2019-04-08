package com.example.cryptomonitor;

import android.app.Activity;
import android.widget.Toast;

public class ExitClass {
    private static long back_pressed = 0;

    public static void onBackPressed(Activity activity) {
        if (System.currentTimeMillis() - back_pressed < 2000)
            activity.finish();
        else
            Toast.makeText(activity.getBaseContext(), activity.getString(R.string.exit_phrase), Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
