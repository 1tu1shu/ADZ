package com.tushu.basedemo;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.appevents.AppEventsLogger;
import com.flurry.android.FlurryAgent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tushu.sdk.TSSDK;


public class MyApplication extends MultiDexApplication{

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Flurry
        initFlurry();
        //初始化FireBase
        FirebaseApp.initializeApp(this);
        FirebaseAnalytics.getInstance(this);
//      FirebaseMessaging.getInstance().subscribeToTopic("touch-en");
//      初始化Facebook
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);

        TSSDK.readFbLog();
        TSSDK.init(this,"2144897019126127_2299753613640466","ca-app-pub-8080140584266451/1091973059");

    }

    private void initFlurry() {
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "6WYN4X9P2MX4X8G45WV2");
    }
}
