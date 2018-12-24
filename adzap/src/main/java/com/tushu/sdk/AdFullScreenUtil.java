package com.tushu.sdk;

import android.content.Context;
import android.util.Log;

import com.tushu.sdk.ad.AdDialog;
import com.tushu.sdk.ad.AdInterstitial;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

public class AdFullScreenUtil {


    public static void showInterAd(Context context,String ad_fb_inter,String ad_admob_inter){
        AdInterstitial.getInstance(context).showAd();
        preLoadInterAd(context,ad_fb_inter,ad_admob_inter);
    }

    /**
     * 加载全屏广告
     */
    public static void showFullScreenAd(Context context,String ad_fb_inter,String ad_admob_inter,String ad_fb_native,String ad_admob_native) {
        int adClassify = SharedPref.getInt(context, SharedPref.LOAD_AD_FULL_SCREEN, 1);
        if (adClassify % 2 == 0) {
            //原生全屏广告
//            showNativeAD(context, admobNativeID, facebookNativeID);
            showNativeAd(context,ad_fb_native,ad_admob_native);
        } else {
            //插屏广告
            showInterAd(context,ad_fb_inter,ad_admob_inter);
        }
        adClassify++;
        SharedPref.setInt(context, SharedPref.LOAD_AD_FULL_SCREEN, adClassify);
    }

    /**
     * 缓存插屏广告
     */
    public static void preLoadInterAd(Context context,String fbId,String admobId) {

//        AdInterstitial.getInstance(context).preLoadAdmob(admobId);
        int adCode = SharedPref.getInt(context, SharedPref.LOAD_AD_PRELOAD, 1);
        if (adCode % 2 == 0) {
            //加载admob
            Log.e("zzz","缓存admob插屏广告:" + admobId);
//            SharedPref.setInt(context, SharedPref.LOAD_AD_PRELOAD, 2);
            AdInterstitial.getInstance(context).preLoadAdmob(admobId);
        } else {
            //加载facebook
            Log.e("zzz","缓存facebook插屏广告:" + fbId);
//            SharedPref.setInt(context, SharedPref.LOAD_AD_PRELOAD, 1);
            AdInterstitial.getInstance(context).preLoadFb(fbId,admobId);
        }
        adCode++;
        SharedPref.setInt(context, SharedPref.LOAD_AD_PRELOAD, adCode);
    }

    /**
     * 展示原生全屏广告
     */
    private static void showNativeAd(Context context,String ad_fb_native,String ad_admob_native) {
        int adCode = SharedPref.getInt(context, SharedPref.LOAD_AD_CODE, 1);
        AdDialog adDialog = new AdDialog(context);
        adDialog.setAdInfo(ad_fb_native, ad_admob_native);
        if (adCode % 2 == 0) {
            //加载admob
            Logger.d("load admob Native全屏广告:" + ad_admob_native);
            adDialog.loadGoogleAd();
        } else {
            //加载facebook
            Logger.d("load facebook Native全屏广告:" + ad_fb_native);
            adDialog.loadNativeAd();
        }
        adCode++;
        SharedPref.setInt(context, SharedPref.LOAD_AD_CODE, adCode);
    }


    /**
     * 缓存Adt插屏广告
     */
    public static void preLoadInterAdt(Context context, String adtId) {
        AdInterstitial.getInstance(context).preLoadAdt(adtId);
    }


    /**
     * 加载Adt全屏广告
     */
    public static void showFullScreenAdt(Context context,String adTimeId,String adFbId) {
        int adClassify = SharedPref.getInt(context, SharedPref.LOAD_AD_FULL_SCREEN, 1);
        if (adClassify % 2 == 0) {
            //原生全屏广告
//            showNativeAD(context, admobNativeID, facebookNativeID);
            new AdDialog(context).setAdInfo(adTimeId,adFbId).loadAdt();
        } else {
            //插屏广告
            AdInterstitial.getInstance(context).showAdt();
//            loadInterstitialAD(context, admobID, facebookID);
            preLoadInterAdt(context,adTimeId);
        }
        adClassify++;
        SharedPref.setInt(context, SharedPref.LOAD_AD_FULL_SCREEN, adClassify);
    }


}