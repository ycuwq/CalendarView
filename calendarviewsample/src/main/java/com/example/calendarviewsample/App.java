package com.example.calendarviewsample;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by ycuwq on 2018/2/28.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

    }
}
