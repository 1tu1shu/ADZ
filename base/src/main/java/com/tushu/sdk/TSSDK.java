package com.tushu.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.aiming.mdt.sdk.AdtAds;
import com.aiming.mdt.sdk.Callback;
import com.tushu.sdk.outad.receive.LockScreenReceiver;
import com.tushu.sdk.outad.receive.NetReceive;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

public class TSSDK {

    public static Application app;
    public static boolean isAdtInit;


    public static void init(Application app,String fbId,String admobId){

        TSSDK.app = app;

        SharedPref.setString(app, SharedPref.AD_FACEBOOK_ID, fbId);
        SharedPref.setString(app, SharedPref.AD_GOOGLE_ID, admobId);

        if (SharedPref.getLong(app, SharedPref.INSTALL_TIME, 0) == 0) {
            //记录安装时间
            Logger.d("安装时间 :" + System.currentTimeMillis());
            SharedPref.setLong(app, SharedPref.INSTALL_TIME, System.currentTimeMillis());
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.setPriority(2147483647);
        app.registerReceiver(new LockScreenReceiver(), filter);
        Log.e("zzz","注册了屏幕监听广播");

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        app.registerReceiver(new NetReceive(), filter2);
        Log.e("zzz","注册了网络监听广播");

    }

    public static void initAdt(Activity activity,String adtKey){
        AdUtil.getServerData(activity);
        if(null!=adtKey) {
            AdtAds.init(activity, adtKey, new Callback() {
                @Override
                public void onSuccess() {
                    // SDK init success
                    isAdtInit = true;
                    Logger.d("ADT初始化成功");
                }

                @Override
                public void onError(String msg) {
                    // SDK init error
                    isAdtInit = false;
                    Logger.d("ADT初始化失败" + msg);
                }
            });
        }
    }

}
