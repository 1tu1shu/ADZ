package com.tushu.basedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tushu.sdk.AdCacheUtil;
import com.tushu.sdk.AdFullScreenUtil;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.ad.AdInterstitial;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TSSDK.initSplash(this,"hO2besnXs9fiycmEtpNVOdn08V5VERL9");
//        adCustom = findViewById(R.id.ad_custom);
//        adCustom.loadAd(MainActivity.this);
//        AdFullScreenUtil.getServerData(this);
//        AdtAds.init(this, "kV7t079yar4D7c6H8NAPgeTXV1a9PIX5", new Callback() {
//            @Override
//            public void onSuccess() {
//                // SDK init success
//                TSSDK.isAdtInit = true;
////                Log.e("zzz","ADT初始化成功");
//            }
//
//            @Override
//            public void onError(String msg) {
//                // SDK init error
////                isAdtInit = false;
//                Log.e("zzz","ADT初始化失败" + msg);
//            }
//        });
//        new AdDialog(MainActivity.this).setAdInfo("203411000521492_203443257184933","ca-app-pub-8080140584266451/6847561682").loadGoogleAd();
        //gradlew clean generatePomFileForReleasePublication build bintrayUpload -PbintrayUser=zjh666 -PbintrayKey=d887fe4c06f0404a4ca4dd93fe1e83f2feb5ee21 -PdryRun=false

//        ca-app-pub-8080140584266451/5833549257
//        ca-app-pub-8080140584266451/2484945776
        AdFullScreenUtil.preLoadInterAd(this,"2144897019126127_2299743200308174","ca-app-pub-8080140584266451/2484945776");


        findViewById(R.id.test_inter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AdFullScreenUtil.showInterAD(MainActivity.this);
                AdInterstitial.getInstance(MainActivity.this).showAd();
                AdFullScreenUtil.preLoadInterAd(MainActivity.this,"2144897019126127_2299743200308174","ca-app-pub-8080140584266451/2484945776");
            }
        });

        findViewById(R.id.test_native).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TestActivity.class));
            }
        });

        AdCacheUtil.saveAdCustom(this,R.layout.adz_banner4,"2144897019126127_2299068017042359","ca-app-pub-8080140584266451/2409349186");

    }
}
