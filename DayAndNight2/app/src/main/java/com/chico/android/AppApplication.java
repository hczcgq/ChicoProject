package com.chico.android;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created on 2016/12/12.
 * Author Chico Chen
 */
public class AppApplication extends Application {

    private static AppApplication sInstance;

    public static AppApplication getsInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.sInstance = this;
        initNightMode();
    }

    protected void initNightMode() {
        boolean isNight = PreferencesUtils.getBoolean(this, Constant.ISNIGHT, false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
