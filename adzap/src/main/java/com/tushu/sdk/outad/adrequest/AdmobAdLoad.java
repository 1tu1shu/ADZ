package com.tushu.sdk.outad.adrequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.AdUtil;
import com.tushu.sdk.outad.OutADDBHelper;
import com.tushu.sdk.outad.activity.AdmobShowActivity;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;
import com.tushu.sdk.utils.UtilUISizeHelper;

import java.util.Random;


public class AdmobAdLoad implements AdLoad {
    private AdLoader adLoader;
    private UnifiedNativeAd admobNativeAd;
    private View adView;
    private Activity activity;

    @Override
    public void loadAd(final Context context, String adID) {
        DotUtil.sendEvent(DotUtil.OUT_AD_ADMOB_REQUEST);
        adLoader = new AdLoader.Builder(context, adID)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        Logger.d("admob加载完成");
                        if (unifiedNativeAd == null) {
                            DotUtil.sendError(context, "admob unifiedNativeAd == null");
                            Logger.d("admob unifiedNativeAd == null");
                            return;
                        }
                        admobNativeAd = unifiedNativeAd;
                        DotUtil.sendEvent(DotUtil.OUT_AD_SHOW);
                        //加载界面
                        loadAdView(context);
                    }
                })
                .withAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Logger.d("admob加载失败 ：" + i);
                        DotUtil.sendError(context, "加载Admob广告错误:" + i);
                        AdManager.getInstence().setShowAd(false);
//                        AdManager.getInstence().loadFacebookAd(context,"1898385083559097_1906518439412428");
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        Logger.d("admob点击");
                        DotUtil.sendEvent(DotUtil.OUT_AD_CLICK);
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                        if (null != activity) activity.finish();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()

                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
        Logger.d("加载admob广告");
        AdManager.getInstence().setShowAd(true);
    }

    UnifiedNativeAdView adParentView;

    @Override
    public void loadAdView(final Context context) {
        AdModel adModel = AdUtil.getAdModel(SharedPref.getString(context, SharedPref.AD_FACEBOOK_ID));

        LayoutInflater inflater = LayoutInflater.from(context);
        adView = inflater.inflate(com.tushu.sdk.R.layout.adz_out_admob_layout, null);

        MediaView ad_media = adView.findViewById(com.tushu.sdk.R.id.ad_media);
        final ImageView ad_close = adView.findViewById(com.tushu.sdk.R.id.ad_close);
        ImageView ad_icon = adView.findViewById(com.tushu.sdk.R.id.ad_icon);
        TextView ad_title = adView.findViewById(com.tushu.sdk.R.id.ad_title);
        TextView ad_desc = adView.findViewById(com.tushu.sdk.R.id.ad_desc);
        TextView ad_open = adView.findViewById(com.tushu.sdk.R.id.ad_open);

        // Show the ad.
        adParentView = new UnifiedNativeAdView(context);
        if (null != ad_title) {
            ad_title.setText(admobNativeAd.getHeadline());
        }
        if (null != ad_desc) {
            ad_desc.setText(admobNativeAd.getBody());
        }
        if (null != ad_open) {
            ad_open.setText(admobNativeAd.getCallToAction());
        }

//        if (null != ad_image && !admobNativeAd.getImages().isEmpty() && admobNativeAd.getImages().get(0).getDrawable() != null) {
//            ad_image.setImageDrawable(admobNativeAd.getImages().get(0).getDrawable());
//        }
        if (null != ad_icon && admobNativeAd.getIcon() != null && admobNativeAd.getIcon().getDrawable() != null) {
            ad_icon.setImageDrawable(admobNativeAd.getIcon().getDrawable());
        }

        //关闭按钮显示时间

        if (adModel != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ad_close.setVisibility(View.VISIBLE);
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
            ad_close.setPadding(padding, padding, padding, padding);
            ad_close.setLayoutParams(params);
        }

        if (null != adModel && adModel.adClickInvalid == 0) {
            if (adModel.descClickable == 1) {
                Logger.d("admob 描述可点");
                adParentView.setBodyView(ad_desc);
            }

            if (adModel.titleClickable == 1) {
                Logger.d("admob title可点");
                adParentView.setHeadlineView(ad_title);
            }

            if (adModel.iconClickable == 1) {
                Logger.d("admob icon可点");
                adParentView.setIconView(ad_icon);
            }

            if (new Random().nextInt(100) <= adModel.coverRate) {
                Logger.d("admob 大图可点");
                adParentView.setMediaView(ad_media);
            }
        }

        adParentView.setCallToActionView(ad_open);

        adParentView.addView(adView);
        adParentView.setNativeAd(admobNativeAd);

        DotUtil.sendEvent(DotUtil.OUT_AD_SHOW_VIEW);
        OutADDBHelper helper = new OutADDBHelper(context);
        String num = helper.getShowNum();
        helper.deleteShowNum(num);
        int newNum = Integer.parseInt(num) + 1;
        helper.saveShowNum(newNum + "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent adIntent = new Intent(context, AdmobShowActivity.class);
                adIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(adIntent);
            }
        }, 3000);
    }

    @Override
    public void addViewToActivity(final Activity activity, ViewGroup viewGroup) {
        this.activity = activity;
        try {
            ViewGroup parent = (ViewGroup) adParentView.getParent();
            if (parent != null) parent.removeAllViews();
            viewGroup.addView(adParentView);
            final ImageView ad_close = adView.findViewById(com.tushu.sdk.R.id.ad_close);
            ad_close.setOnClickListener(new View.OnClickListener() {
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
