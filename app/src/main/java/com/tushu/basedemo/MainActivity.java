package com.tushu.basedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aiming.mdt.sdk.AdtAds;
import com.aiming.mdt.sdk.Callback;
import com.tushu.sdk.AdUtil;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.ad.AdCustom;
import com.tushu.sdk.ad.AdDialog;
import com.tushu.sdk.utils.Logger;


public class MainActivity extends AppCompatActivity {

    private AdCustom adCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TSSDK.initAdt(this,"hO2besnXs9fiycmEtpNVOdn08V5VERL9");
        adCustom = findViewById(R.id.ad_custom);
        AdUtil.getServerData(this);
        AdtAds.init(this, "kV7t079yar4D7c6H8NAPgeTXV1a9PIX5", new Callback() {
            @Override
            public void onSuccess() {
                // SDK init success
                TSSDK.isAdtInit = true;
                adCustom.loadAd(MainActivity.this);
//                new AdDialog(MainActivity.this).setAdtInfo("1566","203411000521492_203443257184933").loadAdt();
                Logger.d("ADT初始化成功");
            }

            @Override
            public void onError(String msg) {
                // SDK init error
//                isAdtInit = false;
                Logger.d("ADT初始化失败" + msg);
            }
        });

        //gradlew clean generatePomFileForReleasePublication build bintrayUpload -PbintrayUser=zjh666 -PbintrayKey=d887fe4c06f0404a4ca4dd93fe1e83f2feb5ee21 -PdryRun=false
    }
}
