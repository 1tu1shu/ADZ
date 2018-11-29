package com.tushu.sdk.ad;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.tushu.sdk.AdUtil;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.utils.DotUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2017/11/27.
 */

public class AdInterstitial {


    private Context context;
    private com.aiming.mdt.sdk.ad.interstitialAd.InterstitialAd adtAd;
    private com.google.android.gms.ads.InterstitialAd admobAd;
    private com.facebook.ads.InterstitialAd fbAd;

    private String adTimeId;
    private String adFbId;
    private String admobId;

    private AdListener adListener = null;
    private boolean isPreLoad =  false;

    private static AdInterstitial mInstance;
    public static AdInterstitial getInstance(Context context){
        if(null==mInstance) {
            mInstance = new AdInterstitial(context);
        }
        return mInstance;
    }

    public void setOnAdListener(AdListener adListener) {
        this.adListener = adListener;
    }

    public interface AdListener {
        void onAdClose();
        void onAdClick();
        void onAdLoad();
        void onError(int code, String e);
    }


    public AdInterstitial(Context context) {
        this.context = context;
    }

    public void loadAdmob(){
        Log.e("zzz","预加载Google插屏-"+"ca-app-pub-8080140584266451/5833549257");
        loadAdmob("ca-app-pub-8080140584266451/5833549257", true);
    }

    public void preloadAdmob(String adId){
        Log.e("zzz", "预加载Google插屏:"+adId);
        loadAdmob(adId, true);
    }

    public void loadAdmob(String adId, final boolean isPreLoad){
        admobAd = new com.google.android.gms.ads.InterstitialAd(context);
        admobAd.setAdUnitId(adId);
        admobAd.setImmersiveMode(true);
        admobAd.setAdListener(new com.google.android.gms.ads.AdListener(){
            @Override
            public void onAdLoaded() {
                if (null!=admobAd&&admobAd.isLoaded()&&!isPreLoad) {
                    admobAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                if(adListener != null) adListener.onAdLoad();
            }
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                if (null!=admobAd) {
//                    admobAd.loadAd(new AdRequest.Builder().build());
                }
                if(adListener != null) adListener.onAdClose();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("zzz","google插屏错了"+i);
            }
        });
        admobAd.loadAd(new AdRequest.Builder().build());
    }

    public void loadAd(String adTimeId){
        this.adTimeId = adTimeId;
        isPreLoad = false;
        loadAdt();
    }

    public void preLoadAdt(String adTimeId){
        this.adTimeId = adTimeId;
        isPreLoad = true;
        loadAdt();
//        loadAdmob();
    }

    public void preLoadAd(String fbId,String admobId){
        isPreLoad = true;
        loadNativeAd(fbId,admobId,true);
    }


    public void loadAd(String adtId,boolean islimit) {
        this.adTimeId = adtId;
        if (islimit) {
//            long time = System.currentTimeMillis();
//            long preTime = SharedPref.getLong(context, adtId, 0);
//            if (preTime == 0) {
//                SharedPref.setLong(context, adtId, System.currentTimeMillis());
//                return;
//            }
//            if (time - preTime >= 12 * 60 * 60 * 1000) {
//                loadAdt();
//                SharedPref.setLong(context, adtId, System.currentTimeMillis());
//            }
        } else {
            loadAdt();
        }
    }

    public void destroy() {
        if (null != fbAd) {
            fbAd.setAdListener(null);
            fbAd.destroy();
            fbAd = null;
        }
        if(null!=adtAd){
            adtAd.destroy(context);
        }
    }

//    public void showAd(){
//        if(null!=adtAd&&adtAd.isReady()&&isPreLoad){
//            adtAd.show(context);
//        }
//    }

    public boolean showAdmob(){
        if (null!=admobAd&&admobAd.isLoaded()) {
            admobAd.show();
            return true;
        }
        return false;
    }

    public boolean showAdt(){
        if (null!=adtAd&&adtAd.isReady()&&isPreLoad) {
            adtAd.show(context);
            return true;
        }
        return false;
    }

    public boolean showAd(){
        if (null!=fbAd&&fbAd.isAdLoaded()&&isPreLoad) {
            fbAd.show();
            return true;
        }else if(null!=admobAd&&admobAd.isLoaded()&&isPreLoad) {
            admobAd.show();
            return true;
        }
        return false;
    }


    public void loadNativeAd(String fbId, final String admobId, final boolean isPreLoad) {
//        if (null == fbAd) {
            String adFbId2 = AdUtil.getAdModel(fbId).screenPlacementId;
            if(TextUtils.isEmpty(adFbId2)){
                adFbId2 = fbId;
            }
            Log.e("zzz","加载Facebook插屏-"+adFbId2);
            fbAd = new InterstitialAd(context, adFbId2);
            fbAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) { }

                @Override
                public void onInterstitialDismissed(Ad ad) { }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e("zzz", "FaceBookAd插屏出错"+adError.getErrorMessage());
                    Log.e("zzz", "加载google插屏-"+admobId);
                    loadAdmob(admobId, isPreLoad);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.e("zzz","加载Facebook插屏成功");
                    if (null != fbAd && fbAd.isAdLoaded()&&!isPreLoad) {
                        try {
                            fbAd.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {}

                @Override
                public void onLoggingImpression(Ad ad) {}

            });
//        }
        fbAd.loadAd();
    }


    public void loadAdt(){
        if(null==adTimeId)return;
        if(!TSSDK.isAdtInit)return;
        String adId = adTimeId;
        Log.e("zzzadt",adId);
        adtAd = new com.aiming.mdt.sdk.ad.interstitialAd.InterstitialAd(context, adId);
        adtAd.setListener(new com.aiming.mdt.sdk.ad.interstitialAd.InterstitialAdListener() {
            @Override
            public void onADReady() {
                if(null!=adtAd&&adtAd.isReady()&&!isPreLoad){
                    adtAd.show(context);
                    DotUtil.sendAD(DotUtil.AD_LOAD,4,adTimeId);
                }
                if(null!=adListener){
                    adListener.onAdLoad();
                }
            }

            @Override
            public void onADClick() {
                if(null!=adListener) {
                    adListener.onAdClick();
                }
                DotUtil.sendAD(DotUtil.AD_CLICK,4,adTimeId);
            }

            @Override
            public void onADFail(String msg) {
                Log.e("zzzadt",msg);
                if(null!=adListener) {
                    adListener.onError(1, msg);
                }
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("adId",adTimeId);
                    jsonObj.put("errorMsg",msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DotUtil.sendEventWithExtra(DotUtil.AD_FAIL,jsonObj);
            }

            @Override
            public void onADClose() {
                adListener.onAdClose();
            }
        });
        adtAd.loadAd(context);
    }
}