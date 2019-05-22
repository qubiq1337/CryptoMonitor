package com.example.cryptomonitor;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors executors;
    private final Executor mDbExecutor;
    private final Executor mMainThreadExecutor;

    private AppExecutors() {
        mMainThreadExecutor = new MainThreadExecutor();
        mDbExecutor = Executors.newSingleThreadExecutor();
    }

    public static AppExecutors getInstance() {
        if (executors == null) {
            executors = new AppExecutors();
        }
        return executors;
    }

    public Executor getDbExecutor() {
        return mDbExecutor;
    }

    public Executor getMainThreadExecutor() {
        return mMainThreadExecutor;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
