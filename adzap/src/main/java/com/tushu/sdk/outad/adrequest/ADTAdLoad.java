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

import com.aiming.mdt.sdk.AdtAds;
import com.aiming.mdt.sdk.Callback;
import com.aiming.mdt.sdk.ad.nativead.MediaView;
import com.aiming.mdt.sdk.ad.nativead.NativeAd;
import com.aiming.mdt.sdk.ad.nativead.NativeAdListener;
import com.aiming.mdt.sdk.ad.nativead.NativeAdView;
import com.aiming.mdt.sdk.bean.AdInfo;
import com.squareup.picasso.Picasso;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.AdUtil;
import com.tushu.sdk.outad.OutADDBHelper;
import com.tushu.sdk.outad.activity.AdtShowActivity;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;
import com.tushu.sdk.utils.UtilUISizeHelper;

import java.util.ArrayList;
import java.util.List;

public class ADTAdLoad implements AdLoad {

    private AdInfo oneAdInfo;
    private NativeAd mNativeAd;
    private View adView;
    private NativeAdView mNativeAdView;
    private Activity activity;

    @Override
    public void loadAd(final Context context, String adID) {
        mNativeAd = new com.aiming.mdt.sdk.ad.nativead.NativeAd(context, adID);
        mNativeAd.setListener(new NativeAdListener() {
            @Override
            public void onADReady(AdInfo adInfo) {
                Logger.d("ADT加载完成");
                if (adInfo == null) {
                    DotUtil.sendError(context, "adInfo == null");
                    Logger.d("adInfo == null");
                    return;
                }
                oneAdInfo = adInfo;
                DotUtil.sendEvent(DotUtil.OUT_AD_SHOW);
                //加载布局
                loadAdView(context);
            }

            @Override
            public void onADClick(AdInfo adInfo) {
                Logger.d("ADT点击");
                DotUtil.sendEvent(DotUtil.OUT_AD_CLICK);
                if (null != activity) activity.finish();
            }

            @Override
            public void onADFail(String msg) {
                Logger.d("ADT加载失败 ：" + msg);
                DotUtil.sendError(context, msg);
                AdManager.getInstence().setShowAd(false);

            }
        });

        if (TSSDK.isAdtInit) {
            Logger.d("加载ADT广告");
            mNativeAd.loadAd(context);
            DotUtil.sendEvent(DotUtil.OUT_AD_ADT_REQUEST);
            AdManager.getInstence().setShowAd(true);
        } else {
            AdtAds.init((Activity) context, AdtAds.getAppKey(context), new Callback() {
                @Override
                public void onSuccess() {
                    TSSDK.isAdtInit = true;
                    Logger.d("加载ADT广告");
                    mNativeAd.loadAd(context);
                    DotUtil.sendEvent(DotUtil.OUT_AD_ADT_REQUEST);
                    AdManager.getInstence().setShowAd(true);
                }

                @Override
                public void onError(String s) {
                    TSSDK.isAdtInit = false;
                }
            });
        }
    }

    @Override
    public void loadAdView(final Context context) {
        AdModel adModel = AdUtil.getAdModel(SharedPref.getString(context, SharedPref.AD_FACEBOOK_ID));

        mNativeAdView = new NativeAdView(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        adView = inflater.inflate(com.tushu.sdk.R.layout.adz_out_adt_layout, null);

        MediaView ad_media = adView.findViewById(com.tushu.sdk.R.id.ad_media);
        final ImageView ad_close = adView.findViewById(com.tushu.sdk.R.id.ad_close);
        ImageView ad_icon = adView.findViewById(com.tushu.sdk.R.id.ad_icon);
        TextView ad_title = adView.findViewById(com.tushu.sdk.R.id.ad_title);
        TextView ad_desc = adView.findViewById(com.tushu.sdk.R.id.ad_desc);
        TextView ad_open = adView.findViewById(com.tushu.sdk.R.id.ad_open);

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

        //title
        if (null != ad_title) ad_title.setText(oneAdInfo.getTitle());
        //描述
        if (null != ad_desc) ad_desc.setText(oneAdInfo.getDesc());
        //按钮
        if (null != ad_open) ad_open.setText(oneAdInfo.getCallToActionText());
        //图片
//        if (null != ad_image && oneAdInfo.getImg() != null && oneAdInfo.getImg().getUrl() != null) {
//            Picasso.get().load(oneAdInfo.getImg().getUrl()).into(ad_image);
//        }
        //icon
        if (null != ad_icon && oneAdInfo.getIconUrl() != null) {
            Picasso.get().load(oneAdInfo.getIconUrl()).into(ad_icon);
        }

        mNativeAdView.addView(adView);

        List<View> views = new ArrayList<>();

        if (null != adModel && adModel.adClickInvalid == 0) {
            if (adModel.titleClickable == 1) {
                Logger.d("adt title可点");
                views.add(ad_title);
            }
            if (adModel.iconClickable == 1) {
                Logger.d("adt icon可点");
                views.add(ad_icon);
            }
            if (adModel.descClickable == 1) {
                Logger.d("facebook 描述可点");
                views.add(ad_desc);
            }
//            if (new Random().nextInt(100) <= adModel.coverRate) {
//                Logger.d("facebook 大图可点");
//                views.add(ad_image);
//            }
        }

        if (null != ad_open) views.add(ad_open);

        mNativeAdView.setCallToActionViews(mNativeAd,ad_media,views);

        DotUtil.sendEvent(DotUtil.OUT_AD_SHOW_VIEW);
        OutADDBHelper helper = new OutADDBHelper(context);
        String num = helper.getShowNum();
        helper.deleteShowNum(num);
        int newNum = Integer.parseInt(num) + 1;
        helper.saveShowNum(newNum + "");

        //显示界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent adIntent = new Intent(context, AdtShowActivity.class);
                adIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(adIntent);
            }
        }, 3000);

    }

    @Override
    public void addViewToActivity(final Activity activity, ViewGroup viewGroup) {
        this.activity = activity;
        ViewGroup parent = (ViewGroup) mNativeAdView.getParent();
        if (parent != null) parent.removeAllViews();
        viewGroup.addView(mNativeAdView);
        final ImageView ad_close = adView.findViewById(com.tushu.sdk.R.id.ad_close);
        ad_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }
}
