package com.tushu.sdk.outad.adrequest;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.tushu.sdk.outad.OutADDBHelper;
import com.tushu.sdk.outad.activity.WebGameActivity;
import com.tushu.sdk.utils.ResourceUtil;

import java.io.File;
import java.io.FileInputStream;


public class GameLoad {

    private WebView webview;
    private boolean isLoad;
//    private String loadUrl = "http://zx-h5.h5games.top";
    private String loadUrl = "http://zx-h5.h5games.top/98.html";

    public void loadGame(final Context context,String url) {
        webview = new WebView(context);
        WebSettings webSettings = webview.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
//        initWebViewCache(webSettings);

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

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("zzz", "弹" + view.getUrl());
//                    if (!isLoad) {
//                        isLoad = true;
//                    Log.e("zzz", "加载完了"+view.getUrl());
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent1 = new Intent(context, WebGameActivity.class);
//                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent1);
//                        }
//                    }, 1000);
//                    }

            }
//            }
        });

//        //404处理
//        webview.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
////                view.loadUrl("file:///android_asset/makeuper_404.html");
//            }
//        });

        //加载进度
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 70 && !isLoad) {
                    isLoad = true;
                    Intent intent1 = new Intent(context, WebGameActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);

                    OutADDBHelper helper = new OutADDBHelper(context);
                    String num = helper.getShowNum();
                    helper.deleteShowNum(num);
                    int newNum = Integer.parseInt(num) + 1;
                    helper.saveShowNum(newNum + "");

                }
                Log.e("zzz", newProgress + "");
            }
        });
        webview.loadUrl(url);

    }

    public void addViewToActivity(FrameLayout fl){
        if(null!=fl&&null!=webview) {
            if(null!=webview.getParent()) {
                ((FrameLayout)webview.getParent()).removeAllViews();
            }
            fl.addView(webview);
        }
    }


    public void destroy(){
        isLoad = false;
        if(null!=webview) {
            webview.destroy();
        }
    }

    public void resetShow(){
        isLoad = false;
    }

    private void initWebViewCache(WebSettings webSettings) {
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
//        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache";
        //设置数据库缓存路径
//        webSettings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
//        webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
    }

}