package com.tushu.sdk.ad;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiming.mdt.sdk.AdtAds;
import com.aiming.mdt.sdk.Callback;
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
import com.tushu.sdk.AdUtil;
import com.tushu.sdk.R;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.utils.DotUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Administrator on 2017/11/15.
 */

public class AdDialog extends Dialog{

    private ImageView ad_image,ad_icon;
    private TextView ad_title,ad_desc,ad_open;
    private AdIconView ad_fb_icon;
    private MediaView ad_fb_media;
    private com.aiming.mdt.sdk.ad.nativead.MediaView ad_adt_media;
    private com.google.android.gms.ads.formats.MediaView ad_admob_media;
    private LinearLayout ad_choices;
    private Context context;

    private LinearLayout ad_ll;
    private RelativeLayout ad_rl;
    private FrameLayout ad_fl,ad_root;
    private ImageView ad_close;

    private String adTimeId;
    private String admobId;
    private String adFbId;
    private com.aiming.mdt.sdk.ad.nativead.NativeAd mNativeAd;
    private NativeAdView mNativeAdView;
    private NativeAd fbAd;

    public AdDialog(Context context) {
        super(context, R.style.dialog_screen);
        this.context = context;
    }

    public AdDialog(Context context, int themeResId) {
        super(context, R.style.dialog_screen);
    }

    public AdDialog setAdInfo(String adFbId,String admobId){
        this.adFbId = adFbId;
        this.admobId = admobId;
        return this;
    }

    public AdDialog setAdtInfo(String adTimeId,String adFbId){
        this.adTimeId = adTimeId;
        this.adFbId = adFbId;
        return this;
    }

    public void loadAd(){
        loadNativeAd();
    }

    public void loadAdt(){
        loadAdt(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tushu.sdk.R.layout.adz_dialog);
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        lp.height = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        dialogWindow.setAttributes(lp);
    }

    public boolean show2(){
        try{
            show();
            ad_root = findViewById(com.tushu.sdk.R.id.ad_side_fl);
            ad_ll = findViewById(com.tushu.sdk.R.id.ad_ll);
            ad_rl = findViewById(com.tushu.sdk.R.id.ad_rl);
//            ad_fl = findViewById(R.id.ad_fl);
            ad_icon =  findViewById(com.tushu.sdk.R.id.ad_icon);
            ad_fb_icon = findViewById(R.id.ad_fb_icon);
            ad_fb_media = findViewById(R.id.ad_fb_media);
            ad_adt_media = findViewById(R.id.ad_adt_media);
            ad_admob_media = findViewById(R.id.ad_admob_media);
            ad_choices = findViewById(com.tushu.sdk.R.id.ad_choices);
            ad_image = findViewById(com.tushu.sdk.R.id.ad_image);
            ad_title = findViewById(com.tushu.sdk.R.id.ad_title);
            ad_desc = findViewById(com.tushu.sdk.R.id.ad_desc);
            ad_open = findViewById(com.tushu.sdk.R.id.ad_open);
            ad_close = findViewById(com.tushu.sdk.R.id.ad_close);
            ad_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
//            findViewById(R.id.ad_side_fl).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });

            if(AdUtil.getAdModel(adFbId).bigImgClickable==1){
                int size = context.getResources().getDimensionPixelSize(com.tushu.sdk.R.dimen.h70dp);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size,size);
                params.rightMargin = 0;
                params.topMargin = 0;
                params.gravity = Gravity.RIGHT;

                int padding = context.getResources().getDimensionPixelSize(com.tushu.sdk.R.dimen.w15dp);
                ad_close.setPadding(padding,padding,padding,padding);
                ad_close.setLayoutParams(params);
            }
            ad_close.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ad_close.setVisibility(View.VISIBLE);
                }
            },AdUtil.getAdModel(adFbId).closeBtnTime);

            return true;
        }catch (Exception e){
            Log.e("zzz",e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void loadNativeAd() {
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
                nativeAdLayout.addView(ad_root);

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
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e("zzz", "Facebook加载失败-" + adFbId + "错误：" + adError.getErrorMessage());
                loadGoogleAd();
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
            }

            @Override
            public void onLoggingImpression(Ad ad) { }
            @Override
            public void onMediaDownloaded(Ad ad) { }
        });
        fbAd.loadAd(NativeAd.MediaCacheFlag.ALL);
    }


    public void loadGoogleAd() {
        Log.e("zzz","加载Google-"+admobId);
        AdLoader adLoader = new AdLoader.Builder(context, admobId)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        Log.e("zzz","Google加载成功-"+admobId);
                        // Show the ad.
                        show2();
                        UnifiedNativeAdView adView = new UnifiedNativeAdView(getContext());
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

//                        if (null != ad_image && !unifiedNativeAd.getImages().isEmpty() && unifiedNativeAd.getImages().get(0).getDrawable() != null) {
//                            ad_image.setImageDrawable(unifiedNativeAd.getImages().get(0).getDrawable());
//                            adView.setImageView(ad_image);

//                        }
                        if (null != ad_icon && unifiedNativeAd.getIcon()!= null && unifiedNativeAd.getIcon().getDrawable() !=null) {
                            ad_icon.setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
//                            adView.setIconView(ad_icon);
                        }

                        AdModel adModel = AdUtil.getAdModel(adFbId);
                        if(adModel.adClickInvalid==0) {
                            if (null != ad_title && adModel.titleClickable==1)  adView.setHeadlineView(ad_title);
                            if (null != ad_desc && adModel.descClickable==1)  adView.setBodyView(ad_desc);
                            if (null != ad_admob_media && new Random().nextInt(100) <= adModel.coverRate) adView.setMediaView(ad_admob_media);
                            if (null != ad_icon && adModel.iconClickable==1) adView.setIconView(ad_icon);
                        }

                        if (null != ad_open)  adView.setCallToActionView(ad_open);

                        if (null!= ad_rl.getParent()) {
                            ((FrameLayout) ad_rl.getParent()).removeAllViews();
                        }
                        adView.addView(ad_rl);
                        adView.setNativeAd(unifiedNativeAd);
                        ad_root.addView(adView);

                    }
                })
                .withAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("adId",admobId);
                            jsonObj.put("errorMsg",errorCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DotUtil.sendEventWithExtra("ad_error",jsonObj);
                    }

                    @Override
                    public void onAdClicked() {
                        Log.e("zzz","点击了");
                    }

                    @Override
                    public void onAdLoaded() {
                    }

                    @Override
                    public void onAdOpened() {
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void loadAdt(final Context context){
        String adId = adTimeId;
        mNativeAdView = new NativeAdView(context);
        mNativeAd = new com.aiming.mdt.sdk.ad.nativead.NativeAd(context, adId);
        mNativeAd.setListener(new NativeAdListener() {
            @Override
            public void onADReady(AdInfo adInfo) {
                show2();
                if (null != ad_title) ad_title.setText(adInfo.getTitle());
                if (null != ad_desc) ad_desc.setText(adInfo.getDesc());
                if (null != ad_open) ad_open.setText(adInfo.getCallToActionText());
//                if (null != ad_image && adInfo.getImg() != null && adInfo.getImg().getUrl() != null) {
//                    Picasso.get().load(adInfo.getImg().getUrl()).into(ad_image);
//                }
                if (null != ad_icon && adInfo.getIconUrl() != null) {
                    Picasso.get().load(adInfo.getIconUrl()).into(ad_icon);
                }
                if (ad_rl.getParent() != null) {
                    ((FrameLayout) ad_rl.getParent()).removeAllViews();
                }
                mNativeAdView.addView(ad_rl);
                List views = new ArrayList<>();

                AdModel adModel = AdUtil.getAdModel(adFbId);
                if(adModel.adClickInvalid==0) {
                    if (null != ad_title && adModel.titleClickable==1)  views.add(ad_title);
                    if (null != ad_desc && adModel.descClickable==1)  views.add(ad_desc);
//                    if (null != ad_image && new Random().nextInt(100) <= adModel.coverRate) views.add(ad_image);
                    if (null != ad_icon && adModel.iconClickable==1) views.add(ad_icon);
                }

                if(null!=ad_open)views.add(ad_open);
                mNativeAdView.setCallToActionViews(mNativeAd,ad_adt_media,views);
                ad_root.addView(mNativeAdView);
                DotUtil.sendAD(DotUtil.AD_LOAD,adInfo.getType(),adTimeId);
            }

            @Override
            public void onADClick(AdInfo adInfo) {
                DotUtil.sendAD(DotUtil.AD_CLICK,adInfo.getType(),adTimeId);
                dismiss();
            }

            @Override
            public void onADFail(String msg) {
                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("adId",adTimeId);
                    jsonObj.put("errorMsg",msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DotUtil.sendEventWithExtra(DotUtil.AD_FAIL,jsonObj);
                Log.e("zzzadt", msg);
            }
        });
        if (TSSDK.isAdtInit) {
            mNativeAd.loadAd(context);
        } else {
            AdtAds.init((Activity) context, AdtAds.getAppKey(context), new Callback() {
                @Override
                public void onSuccess() {
                    Log.e("zzzadt", "success-2");
                    mNativeAd.loadAd(context);
                }

                @Override
                public void onError(String s) {
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK&&AdUtil.getAdModel(adFbId).backClickable==0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
