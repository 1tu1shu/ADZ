package com.tushu.sdk;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class AdDelayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        Window mWindow = getWindow();
//        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
//        attrParams.x = 0;
//        attrParams.y = 0;
//        attrParams.height = 1;
//        attrParams.width = 1;
//        mWindow.setAttributes(attrParams);
        setContentView(R.layout.activity_ad_delay);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
