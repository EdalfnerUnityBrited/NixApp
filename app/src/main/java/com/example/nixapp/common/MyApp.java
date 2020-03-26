package com.example.nixapp.common;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowManager;


@Database(name = MyApp.NAME, version = MyApp.VERSION)
public class MyApp extends Application {
    public static final String NAME = "nixdb";

    public static final int VERSION = 1;
    private static  MyApp instance;

    public static MyApp getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        instance =this;
        super.onCreate();
        FlowManager.init(this);
    }


}
