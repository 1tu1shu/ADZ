package com.tushu.sdk.outad.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.tushu.sdk.R;
import com.tushu.sdk.outad.adrequest.AdManager;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.ResourceUtil;

import java.io.IOException;


/**
 * web游戏
 */
public class WebGameActivity extends AppCompatActivity {

    private FrameLayout fl_game;
    private long gameTime = 0L;
    private WebView webview;
    private boolean isLoad;
    private String loadUrl = "http://zx-h5.h5games.top";
//    private String loadUrl = "http://zx-h5.h5games.top/98.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webgame);
        DotUtil.sendEvent(DotUtil.OUT_GAME_SHOW);
        gameTime = System.currentTimeMillis();
        loadGame();
//        fl_game = findViewById(R.id.game_fl);
    }


    private void initWebViewCache(WebSettings webSettings) {

//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
//        // 开启 DOM storage API 功能
//        webSettings.setDomStorageEnabled(true);
//        //开启 database storage API 功能
//        webSettings.setDatabaseEnabled(true);
//        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache";
//        //设置数据库缓存路径
//        webSettings.setDatabasePath(cacheDirPath);
//        //设置  Application Caches 缓存目录
//        webSettings.setAppCachePath(cacheDirPath);
//        //开启 Application Caches 功能
//        webSettings.setAppCacheEnabled(true);
    }



    @Override
    protected void onResume() {
        super.onResume();
//        AdManager.getInstence().loadGameView(fl_game);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        if (webview.canGoBack()) {
//            webview.goBack();
//        }else {
//            super.onBackPressed();
//        }
    }


    public void loadGame() {
        webview = findViewById(R.id.game_webgame);
//        initWebViewCache(webSettings);
        WebSettings webSettings = webview.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);

        //webview打开
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse response = ResourceUtil.shouldInterceptRequest(loadUrl,view, url);
                return response == null ? super.shouldInterceptRequest(view, url) : response;
            }
        });

        webview.loadUrl(loadUrl);
    }

}