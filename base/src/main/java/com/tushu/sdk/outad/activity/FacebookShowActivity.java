package com.tushu.sdk.outad.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.AdUtil;
import com.tushu.sdk.outad.adrequest.AdManager;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

/**
 * facebook广告展示界面
 */
public class FacebookShowActivity extends AppCompatActivity {

    private AdModel adModel;

    private long backWaitTime = 0L;
    private boolean backClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(com.tushu.sdk.R.layout.activity_show);
        Logger.d("facebook ad view show");
        DotUtil.sendEvent(DotUtil.OUT_AD_ACTIVITY_SHOW);

        LinearLayout native_ad_container = findViewById(com.tushu.sdk.R.id.native_ad_container);

        AdManager.getInstence().loadFacebookAdView(this, native_ad_container);

        adModel = AdUtil.getAdModel(SharedPref.getString(this, SharedPref.AD_FACEBOOK_ID));

        backWaitTime = adModel.backBtnTime;
        Logger.d("facebook back " + backWaitTime + "毫秒后可点");
        new Thread(new BackThr()).start();
    }

    private class BackThr implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(backWaitTime);
                backClick = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (adModel != null && adModel.backClickable == 0 || !backClick) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AdManager.getInstence().setShowAd(false);
    }
}
