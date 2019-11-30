package com.example.steven.myapplication;

import android.app.Application;

public class MyApplication extends Application {

    public static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

}
