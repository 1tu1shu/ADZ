package com.tushu.sdk;

import android.content.Context;
import android.util.Log;

import com.tushu.sdk.ad.AdDialog;
import com.tushu.sdk.ad.AdInterstitial;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

public class AdFullScreenUtil {

    /**
     * 加载全屏广告
     */
    public static void showFullScreenAD(Context context,String admobID, String facebookID) {
        int adClassify = SharedPref.getInt(context, SharedPref.LOAD_AD_FULL_SCREEN, 1);
        if (adClassify % 2 == 0) {
            //原生全屏广告
//            showNativeAD(context, admobNativeID, facebookNativeID);
            showNativeAD(context, admobID, facebookID);
        } else {
            //插屏广告
            AdInterstitial.getInstance(context).showAd();
//            showInterAD(context);
//            loadInterstitialAD(context, admobID, facebookID);
            preLoadInterAD(context,admobID,facebookID);
        }
        adClassify++;
        SharedPref.setInt(context, SharedPref.LOAD_AD_FULL_SCREEN, adClassify);
    }

    /**
     * 加载Adt全屏广告
     */
    public static void showFullScreenAdt(Context context,String adTimeId,String adFbId) {
        int adClassify = SharedPref.getInt(context, SharedPref.LOAD_AD_FULL_SCREEN, 1);
        if (adClassify % 2 == 0) {
            //原生全屏广告
//            showNativeAD(context, admobNativeID, facebookNativeID);
            new AdDialog(context).setAdtInfo(adTimeId,adFbId).loadAdt();
        } else {
            //插屏广告
            AdInterstitial.getInstance(context).showAdt();
//            loadInterstitialAD(context, admobID, facebookID);
            preLoadInterAdt(context,adTimeId);
        }
        adClassify++;
        SharedPref.setInt(context, SharedPref.LOAD_AD_FULL_SCREEN, adClassify);
    }

    /**
     * 缓存插屏广告
     */
    public static void preLoadInterAD(Context context, String admobID, String facebookID) {
        int adCode = SharedPref.getInt(context, SharedPref.LOAD_AD_PRELOAD, 1);
        if (adCode % 2 == 0) {
            //加载admob
            Log.e("zzz","缓存admob插屏广告:" + admobID);
//            SharedPref.setInt(context, SharedPref.LOAD_AD_PRELOAD, 2);
            AdInterstitial.getInstance(context).preLoadAdmob(admobID);
        } else {
            //加载facebook
            Log.e("zzz","缓存facebook插屏广告:" + facebookID);
//            SharedPref.setInt(context, SharedPref.LOAD_AD_PRELOAD, 1);
            AdInterstitial.getInstance(context).preLoadFb(facebookID,admobID);
        }
        adCode++;
        SharedPref.setInt(context, SharedPref.LOAD_AD_PRELOAD, adCode);
    }

    /**
     * 缓存Adt插屏广告
     */
    public static void preLoadInterAdt(Context context, String adtID) {
        AdInterstitial.getInstance(context).preLoadAdt(adtID);
    }

    /**
     * 展示插屏广告
     */
//    public static boolean showInterAD(Context context) {
//        int reload = SharedPref.getInt(context, SharedPref.LOAD_AD_PRELOAD, 1);
//        if (reload % 2 == 0) {
//            //加载admob
//            Logger.d("展示admob插屏广告");
//            return AdInterstitial.getInstance(context).showAdmob();
//        } else {
//            //加载facebook
//            Logger.d("展示facebook插屏广告");
//            return AdInterstitial.getInstance(context).showFb();
//        }
//    }



    /**
     * 展示原生全屏广告
     */
    private static void showNativeAD(Context context, String admobID, String facebookID) {
        int adCode = SharedPref.getInt(context, SharedPref.LOAD_AD_CODE, 1);
        AdDialog adDialog = new AdDialog(context);
        adDialog.setAdInfo(facebookID, admobID);
        if (adCode % 2 == 0) {
            //加载admob
            Logger.d("load admob Native全屏广告:" + admobID);
            adDialog.loadGoogleAd();
        } else {
            //加载facebook
            Logger.d("load facebook Native全屏广告:" + facebookID);
            adDialog.loadNativeAd();
        }
        adCode++;
        SharedPref.setInt(context, SharedPref.LOAD_AD_CODE, adCode);
    }


}
