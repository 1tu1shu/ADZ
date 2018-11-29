package com.tushu.basedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tushu.sdk.TSSDK;
import com.tushu.sdk.ad.AdInterstitial;
import com.tushu.sdk.ad.AdUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdInterstitial.getInstance(this).preloadAdmob("ca-app-pub-8080140584266451/4774517719");

    }
}
