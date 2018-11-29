package com.tushu.basedemo;

import android.app.Application;

import com.tushu.sdk.TSSDK;

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        TSSDK.init(this,"203411000521492_203442757184983","ca-app-pub-8080140584266451/7819060055");

    }
}
