package com.tushu.sdk.outad.adrequest;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tushu.sdk.AdUtil;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.ad.AdProxy;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;

public class AdManager {

    private static AdManager adManager;

    private boolean showAD = false;

    private boolean hasNet = true;
    private boolean hasUnLock = true;

    private FacebookAdLoad facebookAdLoad;
    private AdmobAdLoad admobAdLoad;
    private ADTAdLoad adtAdLoad;
    private GameLoad gameLoad;

    private AdManager() {
        facebookAdLoad = new FacebookAdLoad();
        admobAdLoad = new AdmobAdLoad();
        adtAdLoad = new ADTAdLoad();
        gameLoad = new GameLoad();
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

    public void loadAd(final Context context) {
        String adFbId = SharedPref.getString(context, SharedPref.AD_FACEBOOK_ID);
        AdProxy.getInstance().loadAd(context, adFbId, new AdProxy.OnTypeCallback() {
            @Override
            public void loadFacebook(String adFbId) {
                loadFacebookAd(context,adFbId);
            }

            @Override
            public void loadGoogle() {
                loadAdmobAd(context, SharedPref.getString(context, SharedPref.AD_GOOGLE_ID));
            }

            @Override
            public void loadADT() {
                loadADTAd(context,"");
            }
        });
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


    //game
    public void loadGame(Context context){
        DotUtil.sendEvent(DotUtil.OUT_GAME_REQUEST);

        JSONArray domain = AdUtil.getAdModel("game").domainArray;

        int gameNum = SharedPref.getIntPrivate(context,"gameNum",0,"adz_preferences");

        gameLoad.resetShow();
        if(null!=domain&&domain.length()>0) {
            gameLoad.loadGame(context, domain.optString(gameNum % domain.length()));
        }else{
            gameLoad.loadGame(context,"http://zx-h5.h5games.top");
        }

        SharedPref.setIntPrivate(context,"gameNum",++gameNum,"adz_preferences");

    }

    public void loadGameView(FrameLayout fl) {
        gameLoad.addViewToActivity(fl);
    }



}
