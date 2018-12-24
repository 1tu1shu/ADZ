package com.tushu.basedemo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tushu.sdk.AdCacheUtil;
import com.tushu.sdk.AdFullScreenUtil;
import com.tushu.sdk.AdUtil;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.ad.AdBase;
import com.tushu.sdk.ad.AdCustom;
import com.tushu.sdk.ad.AdDialog;
import com.tushu.sdk.ad.AdInterstitial;
import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.net.DESUtil;
import com.tushu.sdk.outad.activity.WebGameActivity;
import com.tushu.sdk.outad.adrequest.AdManager;
import com.tushu.sdk.outad.adrequest.GameLoad;
import com.tushu.sdk.utils.Downloader;
import com.tushu.sdk.utils.ResourceUtil;


public class MainActivity extends AppCompatActivity {

    private AdCustom adCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TSSDK.initSplash(this);
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
//        new AdDialog(MainActivity.this).setAdInfo("203411000521492_203443257184933","ca-app-pub-8080140584266451/6847561682").loadAd();
        //gradlew clean generatePomFileForReleasePublication build bintrayUpload -PbintrayUser=zjh666 -PbintrayKey=d887fe4c06f0404a4ca4dd93fe1e83f2feb5ee21 -PdryRun=false

//        ca-app-pub-8080140584266451/5833549257
//        ca-app-pub-8080140584266451/2484945776
//        AdFullScreenUtil.preLoadInterAd(this,"2144897019126127_2299743200308174","ca-app-pub-8080140584266451/2484945776");


//        Log.e("zzz解密", DESUtil.Decrypt("[{\"adClickInvalid\":1,\"adId\":\"23\",\"adtId\":\"23\",\"backBtnTime\":13,\"backClickable\":1,\"bigImgClickable\":1,\"channelName\":\"adtiming\",\"closeBtnTime\":12,\"coverRate\":2323,\"descClickable\":0,\"iconClickable\":0,\"insertIntervalTime\":23,\"insertScreen\":\"2\",\"openAdt\":1,\"preload\":1,\"screenPlacementId\":\"\",\"titleClickable\":0,\"videoIntervalTime\":23}]"));

//        findViewById(R.id.test_inter).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                AdFullScreenUtil.showInterAD(MainActivity.this);
//                AdInterstitial.getInstance(MainActivity.this).showAd();
//                AdFullScreenUtil.preLoadInterAd(MainActivity.this,"2144897019126127_2299743200308174","ca-app-pub-8080140584266451/2484945776");
//            }
//        });
//
//        findViewById(R.id.test_native).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,TestActivity.class));
//            }
//        });

//        AdCacheUtil.saveAdCustom(this,R.layout.adz_banner4,"2144897019126127_2299068017042359","ca-app-pub-8080140584266451/2409349186");

//        View view = AdCacheUtil.getAdCustom("Facebook广告ID");

//        adCustom.loadAd(this);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent1 = new Intent(MainActivity.this,AdDelayActivity.class);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent1);
//            }
//        },2000);

//        startActivity(new Intent(this,WebGameActivity.class));

        findViewById(R.id.game_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AdManager.getInstence().loadGame(MainActivity.this);
                startActivity(new Intent(MainActivity.this,WebGameActivity.class));
            }
        });

        Log.e("zzz",getFilesDir().getPath());


//        ResourceUtil.loadRes(this,"http://zx-h5.h5games.top/98.html");


    }
}
