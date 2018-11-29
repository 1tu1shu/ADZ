package com.tushu.sdk.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.flurry.android.FlurryAgent;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.net.AsyncTaskNew;
import com.tushu.sdk.net.DotProtocol;
import com.tushu.sdk.net.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/27.
 */

public class DotUtil {

    public static final String AD_LOAD = "ad_load"; //广告加载成功
    public static final String AD_CLICK = "ad_click"; //广告点击
    public static final String AD_FAIL = "ad_fail"; //广告错误

    //外插屏广告
    public static final String OUT_AD_SHOW = "out_ad_show"; //广告加载完成
    public static final String OUT_AD_CLICK = "out_ad_click"; //广告点击
    private static final String OUT_AD_ERROR = "out_ad_error"; //广告错误 -> error
    public static final String OUT_AD_ACTIVITY_SHOW = "out_ad_activity_show"; //广告activity进入
    public static final String OUT_AD_SHOW_VIEW = "out_ad_show_view"; //界面展示出来

    public static final String OUT_AD_CHARGE_ACTION = "out_ad_charge_action";//充电条件达到
    public static final String OUT_AD_NETCHANGE_ACTION = "out_ad_netchange_action";//网络变化条件达到
    public static final String OUT_AD_SCREENLOCK_ACTION = "out_ad_screenlock_action";//锁屏条件达到

    public static final String OUT_AD_CHARGE_JUDGE = "out_ad_charge_judge";//充电判断进入
    public static final String OUT_AD_NETCHANGE_JUDGE = "out_ad_netchange_judge";//网络变化判断进入
    public static final String OUT_AD_SCREENLOCK_JUDGE = "out_ad_screenlock_judge";//锁屏判断进入

    public static final String OUT_AD_FACEBOOK_REQUEST = "out_ad_facebook_request";//facebook广告请求
    public static final String OUT_AD_ADMOB_REQUEST = "out_ad_admob_request";//admob广告请求
    public static final String OUT_AD_ADT_REQUEST = "out_ad_adt_request";//adt广告请求

    private static AppEventsLogger logger = AppEventsLogger.newLogger(TSSDK.app);

    public static void sendEvent(String type) {
        Log.e("打点->", type);
        FlurryAgent.logEvent(type);
        logger.logEvent(type);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("type", type);
            jsonObj.put("pkg", TSSDK.app.getPackageName());
            jsonObj.put("dot", true);
            jsonObj.put("imei", DeviceUtil.getUid(TSSDK.app));
            jsonObj.put("versionName", DeviceUtil.getVersionName(TSSDK.app));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AsyncTaskNew<String>(){
            @Override
            protected String doInBackground(String... params) {
                return HttpHelper.doGet(DotProtocol.getDotUrl() + "?p=" + params[0]);
            }
        }.execute(jsonObj.toString());

//        if(TOUCH_START.equals(type) || TOUCH_PANEL_CLICK.equals(type))
//        {
//            FlurryAgent.onStartSession(MainApplication.getInstance());
//        }
//        if (TOUCH_START.equals(type) || TOUCH_PANEL_CLICK.equals(type)) {
//            FlurryAgent.onStartSession(MainApplication.getInstance());
//        }
    }


    public static void sendEventWithExtra(String type, JSONObject extra) {
        Log.e("打点->", type);
        Map<String, String> map = new HashMap<>();
        Bundle params = new Bundle();
        for (Iterator<String> it = extra.keys(); it.hasNext(); ) {
            String key = it.next();
            map.put(key, extra.optString(key));
            params.putString(key, extra.optString(key));
        }
        FlurryAgent.logEvent(type, map);
        logger.logEvent(type, params);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("type", type);
            jsonObj.put("pkg", TSSDK.app.getPackageName());
            jsonObj.put("dot", true);
            jsonObj.put("extra", extra);
            jsonObj.put("imei", DeviceUtil.getUid(TSSDK.app));
            jsonObj.put("versionName", DeviceUtil.getVersionName(TSSDK.app));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AsyncTaskNew<String>() {
            @Override
            protected String doInBackground(String... params) {
                return HttpHelper.doGet(DotProtocol.getDotUrl() + "?p=" + params[0]);
            }
        }.execute(jsonObj.toString());
    }

    public static void sendAD(String type, int platform, String id) {
        Log.e("打点AD->", type + platform + id);
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("type", type);
            jsonObj.put("pkg", TSSDK.app.getPackageName());
            jsonObj.put("dot", true);
            JSONObject jsonOb2 = new JSONObject();
            jsonOb2.put("platform", platform);
            jsonOb2.put("adId", id);
            jsonObj.put("extra", jsonOb2);
            jsonObj.put("imei", DeviceUtil.getUid(TSSDK.app));
            jsonObj.put("versionName", DeviceUtil.getVersionName(TSSDK.app));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AsyncTaskNew<String>() {
            @Override
            protected String doInBackground(String... params) {
                return HttpHelper.doGet(DotProtocol.getDotUrl() + "?p=" + params[0]);
            }
        }.execute(jsonObj.toString());
    }


    public static void sendError(Context context, String error) {
        JSONObject jsonObj = new JSONObject();
        Map<String, String> map = new HashMap<>();
        map.put("error", error);
        FlurryAgent.logEvent(OUT_AD_ERROR, map);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", error);
            jsonObj.put("type", OUT_AD_ERROR);
            jsonObj.put("pkg", context.getPackageName());
            jsonObj.put("dot", true);
            jsonObj.put("extra", jsonObject);
            jsonObj.put("imei", DeviceUtil.getUid(context));
            jsonObj.put("versionName", DeviceUtil.getVersionName(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AsyncTaskNew<String>() {
            @Override
            protected String doInBackground(String... params) {
                return HttpHelper.doGet(DotProtocol.getDotUrl() + "?p=" + params[0]);
            }
        }.execute(jsonObj.toString());
    }


}
