package com.tushu.sdk.outad.adrequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.tushu.sdk.AdUtil;
import com.tushu.sdk.R;
import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.outad.OutADDBHelper;
import com.tushu.sdk.outad.activity.FacebookShowActivity;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;
import com.tushu.sdk.utils.UtilUISizeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FacebookAdLoad implements AdLoad {

    private NativeAd nativeAd;
    private View adView;
    private Activity activity;

    @Override
    public void loadAd(final Context context, String adID) {
        DotUtil.sendEvent(DotUtil.OUT_AD_FACEBOOK_REQUEST);
        AdManager.getInstence().setShowAd(true);
        nativeAd = new NativeAd(context, adID);
        nativeAd.setAdListener(new NativeAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                AdManager.getInstence().setShowAd(false);
                DotUtil.sendError(context, "加载facebook广告错误:" + adError.getErrorMessage());
                Logger.d("加载facebook广告错误:" + adError.getErrorMessage());
                //facebook错误,加载admob
                AdManager.getInstence().loadAdmobAd(context, SharedPref.getString(context, SharedPref.AD_GOOGLE_ID));
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                DotUtil.sendEvent(DotUtil.OUT_AD_SHOW);
                Logger.d("加载facebook广告成功");
                //加载广告布局
                loadAdView(context);
            }

            @Override
            public void onAdClicked(Ad ad) {
                DotUtil.sendEvent(DotUtil.OUT_AD_CLICK);
                Logger.d("点击facebook广告");
                if (null != activity) activity.finish();
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onMediaDownloaded(Ad ad) {

            }

        });

        nativeAd.loadAd();
    }

    @Override
    public void loadAdView(final Context context) {
        AdModel adModel = AdUtil.getAdModel(SharedPref.getString(context, SharedPref.AD_FACEBOOK_ID));

        nativeAd.unregisterView();

        LayoutInflater inflater = LayoutInflater.from(context);
        adView = inflater.inflate(R.layout.adz_out_facebook_layout, null);

        final ImageView native_ad_close = adView.findViewById(R.id.native_ad_close);

        if (adModel != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    native_ad_close.setVisibility(View.VISIBLE);
                }
            }, adModel.closeBtnTime);
        }

        if (adModel.bigImgClickable == 1) {
            int size = UtilUISizeHelper.dpTopx(context, 70);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
            params.rightMargin = 0;
            params.topMargin = 0;
            params.gravity = Gravity.RIGHT;

            int padding = UtilUISizeHelper.dpTopx(context, 15);
            native_ad_close.setPadding(padding, padding, padding, padding);
            native_ad_close.setLayoutParams(params);
        }

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        NativeAdLayout nativeAdLayout = adView.findViewById(R.id.ad_side_fl);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd,nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

//        NativeAd.downloadAndDisplayImage(nativeAd.getAdIcon(), nativeAdIcon);
//        NativeAd.downloadAndDisplayImage(nativeAd.getAdCoverImage(), nativeAdMedia);

        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

        List<View> clickableViews = new ArrayList<>();

        boolean isIconClick = false;
        if (null != adModel && adModel.adClickInvalid == 0) {
            if (adModel.titleClickable == 1) {
                Logger.d("facebook title可点");
                clickableViews.add(nativeAdTitle);
            }

            if (adModel.iconClickable == 1) {
                Logger.d("facebook icon可点");
                isIconClick = true;
//                clickableViews.add(nativeAdIcon);
            }

            if (adModel.descClickable == 1) {
                Logger.d("facebook 描述可点");
                clickableViews.add(nativeAdBody);
            }

            if (new Random().nextInt(100) <= adModel.coverRate) {
                Logger.d("facebook 大图可点");
//                clickableViews.add(nativeAdMedia);
            }
        }

        clickableViews.add(nativeAdCallToAction);

//        if(isIconClick){
            nativeAd.registerViewForInteraction(nativeAdCallToAction,nativeAdMedia,nativeAdIcon,clickableViews);
//        }else{
//            nativeAd.registerViewForInteraction(nativeAdCallToAction,nativeAdMedia,clickableViews);
//        }

//        if (adModel != null && adModel.adClickInvalid == 1) {
//            Random random = new Random();
//            if (random.nextInt(100) <= adModel.coverRate) {
//                Logger.d("facebook 大图可点");
//                clickableViews.add(nativeAdCallToAction);
//                nativeAd.registerViewForInteraction(nativeAdMedia, clickableViews);
//            } else {
//                nativeAd.registerViewForInteraction(nativeAdCallToAction);
//            }
//        } else {
//            Random random = new Random();
//            if (random.nextInt(100) <= adModel.coverRate) {
//                Logger.d("facebook 大图可点");
//                clickableViews.add(nativeAdCallToAction);
//            }
//            nativeAd.registerViewForInteraction(nativeAdMedia, clickableViews);
//        }

        Logger.d("加载facebook广告界面成功");
        DotUtil.sendEvent(DotUtil.OUT_AD_SHOW_VIEW);
        OutADDBHelper helper = new OutADDBHelper(context);
        String num = helper.getShowNum();
        helper.deleteShowNum(num);
        int newNum = Integer.parseInt(num) + 1;
        helper.saveShowNum(newNum + "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳转activity
                Intent adIntent = new Intent(context, FacebookShowActivity.class);
                adIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(adIntent);
            }
        }, 3000);
    }

    @Override
    public void addViewToActivity(final Activity activity, ViewGroup viewGroup) {
        this.activity = activity;
        try {
            ViewGroup parent = (ViewGroup) adView.getParent();
            if (parent != null) parent.removeAllViews();
            viewGroup.addView(adView);
            final ImageView native_ad_close = adView.findViewById(R.id.native_ad_close);
            native_ad_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
