package com.tushu.basedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tushu.sdk.TSSDK;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TSSDK.initAdt(this,"hO2besnXs9fiycmEtpNVOdn08V5VERL9");

        //gradlew clean generatePomFileForReleasePublication build bintrayUpload -PbintrayUser=zjh666 -PbintrayKey=d887fe4c06f0404a4ca4dd93fe1e83f2feb5ee21 -PdryRun=false
    }
}
