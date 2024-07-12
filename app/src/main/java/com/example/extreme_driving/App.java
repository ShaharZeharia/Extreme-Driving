package com.example.extreme_driving;

import android.app.Application;

import com.example.extreme_driving.Utilities.SharePreferencesManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharePreferencesManager.init(this);
    }
}
