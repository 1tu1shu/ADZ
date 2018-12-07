package com.tushu.sdk.outad.adrequest;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

public class AdManager {

    private static AdManager adManager;

    private boolean showAD = false;

    private boolean hasNet = true;
    private boolean hasUnLock = true;

    private FacebookAdLoad facebookAdLoad;
    private AdmobAdLoad admobAdLoad;
    private ADTAdLoad adtAdLoad;

    private AdManager() {
        facebookAdLoad = new FacebookAdLoad();
        admobAdLoad = new AdmobAdLoad();
        adtAdLoad = new ADTAdLoad();
    }

    public static AdManager getInstence() {
        if (adManager == null) {
            synchronized (AdManager.class) {
                if (adManager == null) {
                    adManager = new AdManager();
                }
            }
        }
        return adManager;
    }

    public boolean isHasNet() {
        return hasNet;
    }

    public void setHasNet(boolean hasNet) {
        this.hasNet = hasNet;
    }

    public boolean isHasUnLock() {
        return hasUnLock;
    }

    public void setHasUnLock(boolean hasUnLock) {
        this.hasUnLock = hasUnLock;
    }

    public void setShowAd(boolean showAD) {
        this.showAD = showAD;
    }

    public boolean getShowAd() {
        return showAD;
    }

    public void loadAd(Context context) {
        int adCode = SharedPref.getInt(context, SharedPref.LOAD_AD_CODE, 1);
        if (adCode % 2 == 0) {
            //加载admob
            Logger.d("加载的admob广告外插");
            adCode++;
            SharedPref.setInt(context, SharedPref.LOAD_AD_CODE, adCode);
            loadAdmobAd(context, SharedPref.getString(context, SharedPref.AD_GOOGLE_ID));
        } else {
            //加载facebook
            Logger.d("加载的facebook广告外插");
            adCode++;
            SharedPref.setInt(context, SharedPref.LOAD_AD_CODE, adCode);
            loadFacebookAd(context, SharedPref.getString(context, SharedPref.AD_FACEBOOK_ID));
        }
    }

    //facebook
    private void loadFacebookAd(Context context, String adId) {
        if (hasNet && hasUnLock) {
            Logger.d("加载facebook广告:" + adId);
            facebookAdLoad.loadAd(context, adId);
        }
    }

    public void loadFacebookAdView(Activity activity, ViewGroup viewGroup) {
        facebookAdLoad.addViewToActivity(activity, viewGroup);
    }

    //admob
    public void loadAdmobAd(Context context, String adId) {
        if (hasNet && hasUnLock) {
            Logger.d("加载AdmobAd广告:" + adId);
            admobAdLoad.loadAd(context, adId);
        }
    }

    public void loadAdmobAdView(Activity activity, ViewGroup viewGroup) {
        admobAdLoad.addViewToActivity(activity, viewGroup);
    }

    //adt
    public void loadADTAd(Context context, String adId) {
        adtAdLoad.loadAd(context, adId);
    }

    public void loadADTAdView(Activity activity, ViewGroup viewGroup) {
        adtAdLoad.addViewToActivity(activity, viewGroup);
    }
}
