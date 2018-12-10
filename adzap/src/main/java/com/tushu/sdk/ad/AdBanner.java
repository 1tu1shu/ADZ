package com.tushu.sdk.ad;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.aiming.mdt.sdk.ad.bannerad.BannerAd;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.tushu.sdk.R;
import com.tushu.sdk.utils.SharedPref;


/**
 * Created by A03 on 2018/4/11.
 */

public class AdBanner extends FrameLayout {

    private com.facebook.ads.AdView fbAdView;
    private com.google.android.gms.ads.AdView admobAdView;
    private BannerAd bannerAd;

    private String adTimeId, adFbId, adMobId;
    private boolean adAuto;

    public AdBanner(@NonNull Context context) {
        super(context);
    }

    public AdBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        if(adAuto) loadAd();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AdView);
        adTimeId = a.getString(R.styleable.AdView_ad_timeId);
        adFbId = a.getString(R.styleable.AdView_ad_fbId);
        adMobId = a.getString(R.styleable.AdView_ad_mobId);
        adAuto = a.getBoolean(R.styleable.AdView_ad_auto, true);
    }


    public void loadAd(){
        if(null!=adTimeId){
            loadAdtAd();
        }else{
            /**
            int adCode = SharedPref.getInt(getContext(), SharedPref.LOAD_AD_CODE, 1);
            if (adCode % 2 == 0) {
                Log.e("zzz","加载的admob广告-小banner");
                SharedPref.setInt(getContext(), SharedPref.LOAD_AD_CODE, ++adCode);
                loadAdmobAd();
            } else {
                Log.e("zzz","加载的facebook广告-小banner");
                SharedPref.setInt(getContext(), SharedPref.LOAD_AD_CODE, ++adCode);
                loadFbAd();
            }**/
            loadAdmobAd();
        }
    }

    private void loadFbAd(){
        fbAdView = new com.facebook.ads.AdView(getContext(), adFbId,com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        fbAdView.loadAd();
        addView(fbAdView);
        fbAdView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e("zzz","facebook 小banner出错-"+adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
    }

    private void loadAdmobAd(){
        admobAdView = new com.google.android.gms.ads.AdView(getContext());
        admobAdView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
        admobAdView.setAdUnitId(adMobId);
        admobAdView.loadAd(new AdRequest.Builder().build());
        addView(admobAdView);
        admobAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("zzz","google 小banner出错-"+i);
            }
        });
    }

    private void loadAdtAd(){
        bannerAd = new BannerAd(getContext(), adTimeId);
        bannerAd.loadAd(getContext());
        addView(bannerAd);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (fbAdView != null) {
            fbAdView.destroy();
        }
        if (bannerAd != null) {
            bannerAd.destroy(getContext());
        }
        super.onDetachedFromWindow();
    }
}
