package com.tushu.sdk.ad;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.tushu.sdk.AdDelayActivity;
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

    public void preLoadAdt(String adTimeId){
        this.adTimeId = adTimeId;
        isPreLoad = true;
        loadAdt(adTimeId,isPreLoad);
    }

    public void preLoadAdmob(String admobId){
        this.admobId = admobId;
        isPreLoad = true;
        loadAdmob(admobId,isPreLoad);
    }

    public void preLoadFb(String adFbId){
        preLoadFb(adFbId,null);
    }

    public void preLoadFb(String adFbId,String admobId){
        this.adFbId = adFbId;
        this.admobId = admobId;
        isPreLoad = true;
        loadNativeAd(adFbId,admobId,isPreLoad);
    }

    public void loadAdmob(String admobId){
        loadAdmob(admobId,false);
    }

    public void loadAdmob(final String admobId, final boolean isPreLoad){
        if(TSSDK.isVIP) return;

        admobAd = new com.google.android.gms.ads.InterstitialAd(context);
        admobAd.setAdUnitId(admobId);
        admobAd.setImmersiveMode(true);
        admobAd.setAdListener(new com.google.android.gms.ads.AdListener(){
            @Override
            public void onAdLoaded() {
                if (null!=admobAd&&admobAd.isLoaded()&&!isPreLoad) {
                    admobAd.show();
                } else {
                    Log.e("zzz", "GoogleAd缓存成功"+admobId);
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
            public void onAdOpened() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent(context,AdDelayActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                    }
                },500);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("zzz","google插屏错了"+i);
                if(null!=adListener) adListener.onError(i,"");
            }
        });
        if(!admobAd.isLoading())
        admobAd.loadAd(new AdRequest.Builder().build());
    }

    public void loadNativeAd(String fbId,String admobId){
        loadNativeAd(fbId,admobId,false);
    }

    public void loadNativeAd(final String fbId, final String admobId, final boolean isPreLoad) {
        if(TSSDK.isVIP) return;
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
                public void onInterstitialDismissed(Ad ad) {
                    if(null!=adListener) adListener.onAdClose();
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e("zzz", "FaceBookAd插屏出错"+adError.getErrorMessage());
//                    if(!isPreLoad&&null!=admobId) {
                        Log.e("zzz", "加载google插屏-"+admobId);
                        loadAdmob(admobId, isPreLoad);
                    if(null!=adListener) adListener.onError(adError.getErrorCode(),adError.getErrorMessage());
//                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (null != fbAd && fbAd.isAdLoaded()&&!isPreLoad) {
                        try {
                            fbAd.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(isPreLoad){
                        Log.e("zzz","Facebook缓存成功"+fbId);
                    }
                    if(adListener != null) adListener.onAdLoad();
                }

                @Override
                public void onAdClicked(Ad ad) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent1 = new Intent(context,AdDelayActivity.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                        }
                    },500);

                }

                @Override
                public void onLoggingImpression(Ad ad) {}

            });
//        }
        fbAd.loadAd();
    }


    public void loadAdt(String adTimeId){
        loadAdt(adTimeId,false);
    }

    public void loadAdt(String adTimeId,final boolean isPreLoad){
        if(TSSDK.isVIP) return;
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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent(context,AdDelayActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                    }
                },500);

            }

            @Override
            public void onADFail(String msg) {
                Log.e("zzzadt",msg);
                if(null!=adListener) {
                    adListener.onError(1, msg);
                }
//                JSONObject jsonObj = new JSONObject();
//                try {
//                    jsonObj.put("adId",adTimeId);
//                    jsonObj.put("errorMsg",msg);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                DotUtil.sendEventWithExtra(DotUtil.AD_FAIL,jsonObj);
            }

            @Override
            public void onADClose() {
                adListener.onAdClose();
            }
        });
        adtAd.loadAd(context);
    }

    public boolean showAdmob(){
        if (null!=admobAd&&admobAd.isLoaded()) {
            admobAd.show();
            return true;
        }
        return false;
    }

    public boolean showFb(){
        if (null!=fbAd&&fbAd.isAdLoaded()&&isPreLoad) {
            fbAd.show();
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

}