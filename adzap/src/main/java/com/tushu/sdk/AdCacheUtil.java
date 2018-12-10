package com.tushu.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tushu.sdk.ad.AdBase;
import com.tushu.sdk.ad.AdCustom;
import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.net.AsyncTaskNew;
import com.tushu.sdk.net.HttpHelper;
import com.tushu.sdk.outad.OutADDBHelper;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A03 on 2018/6/7.
 */

public class AdCacheUtil {

    public static Map<String, View> adCacheMap = new HashMap<>();
    public static Map<String, Boolean> adShowMap = new HashMap<>();


    public static void saveAdFbCustom(Context context, final int layoutR, final String fbId, String admobId){

        boolean isShow = false;
        if (null!=adShowMap.get(fbId)){
            isShow = adShowMap.get(fbId);
        }
        if(!isShow) {
            Log.e("zzz","预加载原生广告");
            final AdCustom adCustom = new AdCustom(context) {
                @Override
                protected int getLayout() {
                    return layoutR;
                }
            };
            adCustom.setAdFbId(fbId);
            adCustom.setAdMobId(admobId);
            adCustom.loadNativeAd(fbId);
            adCustom.setOnAdListener(new AdBase.AdListener() {
                @Override
                public void onAdClick() {
                }

                @Override
                public void onAdLoad(int type) {
                    adCacheMap.put(fbId, adCustom);
                    Log.e("zzz", "预加载原生广告成功");
                }

                @Override
                public void onError(String e) {
                    Log.e("zzz", "预加载原生广告失败" + e);
                    adShowMap.put(fbId, false);
                }
            });
            adShowMap.put(fbId, true);
        }
    }


    public static void saveAdCustom(Context context, final int layoutR, final String fbId, String admobId){
        Log.e("zzz","预加载原生广告");

        boolean isShow = false;
        if (null!=adShowMap.get(fbId)){
            isShow = adShowMap.get(fbId);
        }
        if(!isShow) {
            final AdCustom adCustom = new AdCustom(context) {
                @Override
                protected int getLayout() {
                    return layoutR;
                }
            };
            adCustom.setAdFbId(fbId);
            adCustom.setAdMobId(admobId);
            adCustom.loadAd(context);
            adCustom.setOnAdListener(new AdBase.AdListener() {
                @Override
                public void onAdClick() {
                }

                @Override
                public void onAdLoad(int type) {
                    adCacheMap.put(fbId, adCustom);
                    Log.e("zzz", "预加载原生广告成功");
                }

                @Override
                public void onError(String e) {
                    Log.e("zzz", "预加载原生广告失败" + e);
                    adShowMap.put(fbId, false);
                }
            });
            adShowMap.put(fbId, true);
        }
    }


    public static void saveAdtCustom(Context context, final int layoutR, final String adTimeId,final String fbId){
        Log.e("zzz","预加载原生广告"+adTimeId);
        boolean isShow = false;
        if (null!=adShowMap.get(fbId)){
            isShow = adShowMap.get(fbId);
        }
        if(!isShow) {
            final AdCustom adCustom = new AdCustom(context) {
                @Override
                protected int getLayout() {
                    return layoutR;
                }
            };
            adCustom.setAdtId(adTimeId);
            adCustom.setAdFbId(fbId);
            adCustom.loadAd(context);
            adCustom.setOnAdListener(new AdBase.AdListener() {
                @Override
                public void onAdClick() {
                }

                @Override
                public void onAdLoad(int type) {
                    adCacheMap.put(adTimeId, adCustom);
                    Log.e("zzz", "预加载原生广告成功" + adTimeId);
                }

                @Override
                public void onError(String e) {
                    adShowMap.put(fbId, false);
                }
            });
            adShowMap.put(fbId, true);
        }
    }


    public static View getAdCustom(String fbId){
        return adCacheMap.get(fbId);
    }

}