package com.example.realmdemo;

import android.app.Application;

import com.example.realmdemo.db.impl.MHDDBHelper;


/**
 * Created by archerlj on 2017/12/12.
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    
    @Override
    public void onCreate() {
        super.onCreate();

        MHDDBHelper.realmInit(this);
    }
}
