package com.tushu.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.aiming.mdt.sdk.AdtAds;
import com.aiming.mdt.sdk.Callback;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.tushu.sdk.outad.receive.BatteryStatusReceiver;
import com.tushu.sdk.outad.receive.LockScreenReceiver;
import com.tushu.sdk.outad.receive.NetReceive;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static android.content.Intent.ACTION_POWER_CONNECTED;
import static android.content.Intent.ACTION_POWER_DISCONNECTED;

public class TSSDK {

    public static Application app;
    public static String adtKey;
    public static boolean isAdtInit;

    public static boolean isVIP;

    public static void init(Application app){
        TSSDK.app = app;
        AudienceNetworkAds.initialize(app);
    }

    public static void init(Application app,String fbId,String admobId,String adtKey){
        init(app,fbId,admobId,adtKey,null,null,null);
    }

    public static void init(Application app,String fbId,String admobId){
        init(app,fbId,admobId,null,null,null,null);
    }

    public static void init(Application app,String fbId,String admobId,String adtKey
            ,LockScreenReceiver lockScreenReceiver,NetReceive netReceive,BatteryStatusReceiver batteryStatusReceiver){
        TSSDK.app = app;
        TSSDK.adtKey = adtKey;
//      AudienceNetworkAds.isInAdsProcess(app);
        AudienceNetworkAds.initialize(app);

        SharedPref.setString(app, SharedPref.AD_FACEBOOK_ID, fbId);
        SharedPref.setString(app, SharedPref.AD_GOOGLE_ID, admobId);

        if (SharedPref.getLong(app, SharedPref.INSTALL_TIME, 0) == 0) {
            //记录安装时间
            Logger.d("安装时间 :" + System.currentTimeMillis());
            SharedPref.setLong(app, SharedPref.INSTALL_TIME, System.currentTimeMillis());
        }

        registerReceiver(lockScreenReceiver,netReceive,batteryStatusReceiver);
    }


    public static void registerReceiver(LockScreenReceiver lockScreenReceiver,NetReceive netReceive,BatteryStatusReceiver batteryStatusReceiver){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.setPriority(2147483647);
        if(null==lockScreenReceiver) {
            app.registerReceiver(new LockScreenReceiver(), filter);
        }else{
            app.registerReceiver(lockScreenReceiver, filter);
        }
        Log.e("zzz","注册了屏幕监听广播");

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter2.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter2.addAction("android.net.wifi.STATE_CHANGE");
        if(null==netReceive){
            app.registerReceiver(new NetReceive(), filter2);
        }else{
            app.registerReceiver(netReceive, filter2);
        }
        Log.e("zzz","注册了网络监听广播");

        //电池状态变化监听
        IntentFilter filter3 = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        filter3.addAction(ACTION_POWER_CONNECTED);
        filter3.addAction(ACTION_POWER_DISCONNECTED);
        if(null==batteryStatusReceiver){
            app.registerReceiver(new BatteryStatusReceiver(),filter3);
        }else{
            app.registerReceiver(batteryStatusReceiver, filter2);
        }
        Log.e("zzz","注册了电池状态广播");
    }


    public static void initSplash(Activity activity){
        AdUtil.getServerData(activity);
        if(null!=adtKey) {
            AdtAds.init(activity, adtKey, new Callback() {
                @Override
                public void onSuccess() {
                    // SDK init success
                    isAdtInit = true;
                    Log.e("zzz","ADT初始化成功");
                }

                @Override
                public void onError(String msg) {
                    // SDK init error
                    isAdtInit = false;
                    Log.e("zzz","ADT初始化失败" + msg);
                }
            });
        }
    }


    public static void readFbLog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Process mLogcatProc = null;
                    BufferedReader reader = null;
                    try {
                        //获取logcat日志信息
//                        08-24 15:00:21.998 26755-26755/com.wf.junk.clean.file.manage D/AdInternalSettings: Test mode device hash: f170fdf6-aa69-4fec-a46a-8f2da320fb86
                        mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "AdInternalSettings:D *:S"});
                        reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            //logcat打印信息在这里可以监听到
                            if (line.indexOf("Test mode device hash") > 0) {
                                String test = "Test mode device hash: ";
                                String hashCode = line.substring(line.lastIndexOf(test) + test.length());
                                Log.e("zzz添加测试设备ID", hashCode);
                                Looper.prepare();
                                AdSettings.addTestDevice(hashCode);
                                Toast.makeText(TSSDK.app,"注意！已添加Facebook测试设备-"+hashCode,Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }
//                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Toast.makeText(TSSDK.app,"注意！正在添加Facebook测试设备！",Toast.LENGTH_LONG).show();
    }


    public static void isVIP(boolean isVIP){
        TSSDK.isVIP = isVIP;
    }

}
