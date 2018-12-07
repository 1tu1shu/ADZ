package com.tushu.sdk.ad;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiming.mdt.sdk.ad.nativead.NativeAdListener;
import com.aiming.mdt.sdk.ad.nativead.NativeAdView;
import com.aiming.mdt.sdk.bean.AdInfo;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;
import com.tushu.sdk.AdCacheUtil;
import com.tushu.sdk.AdUtil;
import com.tushu.sdk.R;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by A03 on 2018/4/9.
 */

public abstract class AdBase extends FrameLayout {

    protected ViewGroup ad_fl;
    public ImageView ad_image, ad_icon;
    private AdIconView ad_fb_icon;
    private MediaView ad_fb_media;
    private com.google.android.gms.ads.formats.MediaView ad_admob_media;
    private com.aiming.mdt.sdk.ad.nativead.MediaView ad_adt_media;
    public TextView ad_title, ad_desc, ad_open;
    private LinearLayout ad_choices;
    protected com.aiming.mdt.sdk.ad.nativead.NativeAd mNativeAd;
    protected NativeAdView mNativeAdView;

    protected String adTimeId, adFbId, adMobId;
    protected int layout;
    protected boolean adAuto,adCache;

    private View view;

    protected AdListener adListener = null;

    public void setOnAdListener(AdListener adListener) {
        this.adListener = adListener;
    }

    public interface AdListener {
        void onAdClick();

        void onAdLoad(int type);

        void onError(String e);
    }

    public AdBase(@NonNull Context context) {
        super(context);
        initBaseViews();
        initViews();
    }

    public AdBase(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        setVisibility(GONE);
        if(adCache){
            view = AdCacheUtil.getAdCustom(adFbId);
            if(null!=view){
                if(null!=view.getParent()) {
                    ((FrameLayout) view.getParent()).removeAllViews();
                }
                addView(view);
                setVisibility(VISIBLE);
            }
            AdCacheUtil.adShowMap.put(adFbId,false);
        }else {
            initBaseViews();
            initViews();
        }
    }

    //TO预加载

    protected void initViews() {

    }

    private void initBaseViews() {
        view = LayoutInflater.from(getContext()).inflate(getLayout(), null);
        addView(view);
        ad_fl = view.findViewById(R.id.ad_fl);
        ad_icon = view.findViewById(R.id.ad_icon);
        ad_fb_icon = view.findViewById(R.id.ad_fb_icon);
        ad_fb_media = view.findViewById(R.id.ad_fb_media);
        ad_admob_media = view.findViewById(R.id.ad_admob_media);
        ad_adt_media = view.findViewById(R.id.ad_adt_media);
        ad_choices = view.findViewById(R.id.ad_choices);
        ad_image = view.findViewById(R.id.ad_image);
        ad_title = view.findViewById(R.id.ad_title);
        ad_desc = view.findViewById(R.id.ad_desc);
        ad_open = view.findViewById(R.id.ad_open);
        if (adAuto) loadAd(getContext());
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AdView);
//        #6b829b #54a4fa #e8f1fd
//        site = a.getString(R.styleable.AdView_ad_site);
        adTimeId = a.getString(R.styleable.AdView_ad_timeId);
        adFbId = a.getString(R.styleable.AdView_ad_fbId);
        adMobId = a.getString(R.styleable.AdView_ad_mobId);
        layout = a.getResourceId(R.styleable.AdView_ad_layout, 0);
        adAuto = a.getBoolean(R.styleable.AdView_ad_auto, true);
        adCache = a.getBoolean(R.styleable.AdView_ad_cache, false);
        a.recycle();
    }

    protected abstract int getLayout();

    public void loadAd(Context context) {
        if(null!=adTimeId) {
            loadAdt(context);
        }else {
            loadAdc(context);
        }
    }

    public void loadAdc(Context context) {

        int adCode = SharedPref.getInt(context, SharedPref.LOAD_AD_CODE, 1);
        if (adCode % 2 == 0) {
            Log.e("zzz","加载的admob广告-大banner");
            SharedPref.setInt(context, SharedPref.LOAD_AD_CODE, ++adCode);
            if (null != adMobId) {
                loadGoogleAd();
            }
        } else {
            Log.e("zzz","加载的facebook广告-大banner");
            SharedPref.setInt(context, SharedPref.LOAD_AD_CODE, ++adCode);
            if (null != adFbId) {
                String adFbId2 = AdUtil.getAdModel(adFbId).screenPlacementId;
                if (!TextUtils.isEmpty(adFbId2)) {
                    Log.e("zzz", "Facebook拿到网络ID" + adFbId2);
                    loadNativeAd(adFbId2);
                } else {
                    Log.e("zzz", "Facebook用默认ID" + adFbId);
                    loadNativeAd(adFbId);
                }
            }
        }

    }

    protected void loadAdt(final Context context) {
        if (null == adTimeId) {
            return;
        }
        Log.e("zzzadt开始加载", adTimeId);
        String adId = adTimeId;

        mNativeAdView = new NativeAdView(context);
        mNativeAd = new com.aiming.mdt.sdk.ad.nativead.NativeAd(context, adId);
        mNativeAd.setListener(new NativeAdListener() {
            @Override
            public void onADReady(AdInfo adInfo) {
                Log.e("zzzadt", "加载成功-" + adInfo.getType());

                if (null != ad_title) ad_title.setText(adInfo.getTitle());
                if (null != ad_desc) ad_desc.setText(adInfo.getDesc());
                if (null != ad_open) ad_open.setText(adInfo.getCallToActionText());
//                if (null != ad_image && adInfo.getImg() != null && adInfo.getImg().getUrl() != null) {
//                    Picasso.get().load(adInfo.getImg().getUrl()).into(ad_image);
//                }
                if (null != ad_icon && adInfo.getIconUrl() != null) {
                    Picasso.get().load(adInfo.getIconUrl()).into(ad_icon);
                }
                setVisibility(VISIBLE);
                if (view.getParent() != null) {
                    ((FrameLayout) view.getParent()).removeAllViews();
                }
//                if(adInfo.getType()==2&&null!=ad_choices){
//                    ad_choices.setVisibility(VISIBLE);
//                }
                mNativeAdView.addView(view);
                List<View> views = new ArrayList<>();
                AdModel adModel = AdUtil.getAdModel(adFbId);
                if (adModel.adClickInvalid == 0) {
                    if (null != ad_icon && adModel.iconClickable == 1) views.add(ad_icon);
//                    if (null != ad_image && new Random().nextInt(100) <= adModel.coverRate) views.add(ad_image);
                    if (null != ad_title && adModel.titleClickable == 1) views.add(ad_title);
                    if (null != ad_desc && adModel.descClickable == 1) views.add(ad_desc);
                }
                if (null != ad_open) views.add(ad_open);
                mNativeAdView.setCallToActionViews(mNativeAd,ad_adt_media,views);
                addView(mNativeAdView);
                onAdLoad(adInfo.getType());
            }

            @Override
            public void onADClick(AdInfo adInfo) {
                if (adListener != null) {
                    adListener.onAdClick();
                }
//                DotUtil.sendAD(DotUtil.AD_CLICK,adInfo.getType(),adTimeId);
            }

            @Override
            public void onADFail(String msg) {
                Log.e("zzzadt加载出错" + adTimeId, msg);
                if (adListener != null) {
                    adListener.onError(msg);
                }
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("adId", adTimeId);
                    jsonObj.put("errorMsg", msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DotUtil.sendEventWithExtra(DotUtil.AD_FAIL, jsonObj);
            }
        });
        if (TSSDK.isAdtInit) {
            mNativeAd.loadAd(context);
        } else {
            Log.e("zzzadt", "adt初始化失败了,切换plan B->FaceBook");
            if (null != adFbId) loadNativeAd(adFbId);
//            AdtAds.init(context, "cAPsbVtcbeMZZUx8vGptBGfufpsZnx6e", new Callback() {
//                @Override
//                public void onSuccess() {
//                    Log.e("zzzadt", "adt初始化success-2");
//                    MainApplication.getInstance().isAdtInit = true;
//                    mNativeAd.loadAd(context);
//                }
//                @Override
//                public void onError(String s) {
//                    Log.e("zzzadt初始化失败",s);
//                    if (adListener != null) {
//                        adListener.onError(s);
//                    }
//                }
//            });
        }
    }


    private NativeAd fbAd;

    public void loadNativeAd(final String adFbId) {
//        String adId = "1805011946473276_1805034216471049";
        Log.e("zzz", "加载Facebook-" + adFbId);
        fbAd = new NativeAd(getContext(), adFbId);
        fbAd.setAdListener(new com.facebook.ads.NativeAdListener() {
            @Override
            public void onAdLoaded(Ad ad) {

                if (fbAd == null || fbAd != ad) {
                    return;
                }

                fbAd.unregisterView();

                // Add the Ad view into the ad container.
                NativeAdLayout nativeAdLayout = new NativeAdLayout(getContext());
//                nativeAdLayout.addView(view);
                if (null!=view&&view.getParent() != null) {
                    ((FrameLayout) view.getParent()).removeAllViews();
                    nativeAdLayout.addView(view);
                    addView(nativeAdLayout);
                }

                // Add the AdOptionsView
                AdOptionsView adOptionsView = new AdOptionsView(getContext(), fbAd, nativeAdLayout);
                ad_choices.removeAllViews();
                ad_choices.addView(adOptionsView, 0);

                String titleForAd = fbAd.getAdvertiserName();
                String titleForAdButton = fbAd.getAdCallToAction();
                String textForAdBody = fbAd.getAdBodyText();
//                NativeAd.Image coverImage = fbAd.getAdCoverImage();
//                NativeAd.Image adIcon = fbAd.getAdIcon();
//                NativeAd.Image choicesIcon = fbAd.getAdChoicesIcon();
//              String socialContextForAd = nativeAd.getAdSocialContext();

                if (null != ad_title) ad_title.setText(titleForAd);
                if (null != ad_desc) ad_desc.setText(textForAdBody);
                if (null != ad_open) ad_open.setText(titleForAdButton);

//                if (null != ad_icon && null != adIcon) NativeAd.downloadAndDisplayImage(adIcon, ad_icon);
//                if (null != ad_image && null != coverImage) NativeAd.downloadAndDisplayImage(coverImage, ad_image);
//                if (null != ad_choices  && null != choicesIcon) {
//                    ad_choices.setVisibility(VISIBLE);
//                    NativeAd.downloadAndDisplayImage(choicesIcon, ad_choices);
//                }

                setVisibility(VISIBLE);

                List<View> views = new ArrayList<>();

                boolean isIconClick = false;

                AdModel adModel = AdUtil.getAdModel(adFbId);
                if(adModel.adClickInvalid==0) {
//                    if (null != ad_icon && adModel.iconClickable==1) isIconClick = true;
//                    if (null != ad_image && new Random().nextInt(100) <= adModel.coverRate) views.add(ad_image);
                    if (null != ad_title && adModel.titleClickable==1) views.add(ad_title);
                    if (null != ad_desc && adModel.descClickable==1) views.add(ad_desc);
                }

                if (null != ad_open) views.add(ad_open);

//                if(isIconClick){
                    fbAd.registerViewForInteraction(ad_open,ad_fb_media,ad_fb_icon,views);
//                }else{
//                    fbAd.registerViewForInteraction(ad_open,ad_fb_media,views);
//                }
                onAdLoad(2);

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e("zzz", "Facebook加载失败-" + adFbId + "错误：" + adError.getErrorMessage());
                loadGoogleAd();
                if (adListener != null) {
                    adListener.onError(adError.getErrorMessage());
                }
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("adId", adFbId);
                    jsonObj.put("errorMsg", adError.getErrorMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DotUtil.sendEventWithExtra("ad_error", jsonObj);
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (adListener != null) {
                    adListener.onAdClick();
                }
            }



            @Override
            public void onLoggingImpression(Ad ad) { }
            @Override
            public void onMediaDownloaded(Ad ad) { }
        });
        fbAd.loadAd(NativeAd.MediaCacheFlag.ALL);
    }


    public void loadGoogleAd() {
        loadGoogleAd(getContext(), adMobId);
    }

    //    ca-app-pub-8080140584266451/9368166904
    public void loadGoogleAd(final Context context, final String adId) {
        Log.e("zzz", "加载Google-" + adId);
        AdLoader adLoader = new AdLoader.Builder(context, adId)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        Log.e("zzz", "Google加载成功-" + adId);
                        // Show the ad.
                        UnifiedNativeAdView adView = new UnifiedNativeAdView(context);
                        if (null != ad_title) {
                            ad_title.setText(unifiedNativeAd.getHeadline());
//                            adView.setHeadlineView(ad_title);
                        }
                        if (null != ad_desc) {
                            ad_desc.setText(unifiedNativeAd.getBody());
//                            adView.setBodyView(ad_desc);
                        }
                        if (null != ad_open) {
                            ad_open.setText(unifiedNativeAd.getCallToAction());
//                            adView.setCallToActionView(ad_open);
                        }
                        if (null != ad_image && !unifiedNativeAd.getImages().isEmpty() && unifiedNativeAd.getImages().get(0).getDrawable() != null) {
                            ad_image.setImageDrawable(unifiedNativeAd.getImages().get(0).getDrawable());
//                            adView.setImageView(ad_image);
                        }
                        if (null != ad_icon && unifiedNativeAd.getIcon() != null && unifiedNativeAd.getIcon().getDrawable() != null) {
                            ad_icon.setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
//                            adView.setIconView(ad_icon);
                        }

                        setVisibility(VISIBLE);

                        AdModel adModel = AdUtil.getAdModel(adFbId);
                        if (adModel.adClickInvalid == 0) {
                            if (null != ad_title && adModel.titleClickable == 1) adView.setHeadlineView(ad_title);
                            if (null != ad_desc && adModel.descClickable == 1) adView.setBodyView(ad_desc);
                            if (null != ad_image && new Random().nextInt(100) <= adModel.coverRate) adView.setImageView(ad_image);
                            if (null != ad_icon && adModel.iconClickable == 1) adView.setIconView(ad_icon);
                        }

                        if (null != ad_open) adView.setCallToActionView(ad_open);

                        adView.setNativeAd(unifiedNativeAd);

                        if (null!=view&&view.getParent() != null) {
                            ((FrameLayout) view.getParent()).removeAllViews();
                            adView.addView(view);
                            adView.setNativeAd(unifiedNativeAd);
                            addView(adView);
                        }
                        onAdLoad(1);
                    }
                })
                .withAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
//                        if(null!=adFbId) {
//                            Log.e("zzzs","Google加载失败-"+adId+"错误："+errorCode);
//                            loadNativeAd(adFbId);
//                        }
                        Log.e("zzz","Google加载失败-"+adId+"错误："+errorCode);
                        if (adListener != null) {
                            adListener.onError(errorCode + "");
                        }
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("adId", adId);
                            jsonObj.put("errorMsg", errorCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DotUtil.sendEventWithExtra("ad_error", jsonObj);
                    }

                    @Override
                    public void onAdClicked() {
                        Log.e("zzz", "点击了");
                        if (adListener != null) {
                            adListener.onAdClick();
                        }
                    }

                    @Override
                    public void onAdLoaded() {
//                        Log.e("zzzs","Google加载完成"+adId);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public String getFbId() {
        return adFbId;
    }

    public void setAdFbId(String adFbId) {
        this.adFbId = adFbId;
    }

    public void setAdMobId(String adMobId) {
        this.adMobId = adMobId;
    }

    public void setAdtId(String adTimeId) {
        this.adTimeId = adTimeId;
    }


    private void onAdLoad(int type) {
        if (adListener != null) {
            adListener.onAdLoad(type);
        }
//        DotUtil.sendAD(DotUtil.AD_LOAD,type,adFbId);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mNativeAd != null) {
            mNativeAd.destroy(getContext());
        }
        super.onDetachedFromWindow();
    }
}