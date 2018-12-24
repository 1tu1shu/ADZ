package com.tushu.sdk.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceUtil {

    private static final Pattern[] REGEX = {
            Pattern.compile("<link.+href=['\"]([^'\"]+)['\"]"),
            Pattern.compile("\\s+src=['\"]([^'\"]+)['\"]"),
            Pattern.compile("url\\(['\"]?([^'\")]+)['\"]?\\)")
    };

    public static void preloadHtml(Context context,String[] domains){
        for(String url:domains){
            loadRes(context,url);
        }
    }

    public static void loadRes(Context context, String url){
        final String rootPath = context.getFilesDir()+ File.separator+Base64.MD5(url);
        if(!new File(rootPath).exists()) {
            Downloader.getInstance().download(url, rootPath, new Downloader.OnDownloadCallback() {
                @Override
                public void onSuccess(String path) {
                    Log.e("zzzhtmlPath", path);
                    downloadRes(getResourceUrl(path), rootPath);
                }

                @Override
                public void onFailed() { }
            });
        }
    }

    public static void downloadRes(Set<String> urls,String path){
        for(String url:urls){
            Log.e("zzzUrl",url);
            Downloader.getInstance().download(url, path, new Downloader.OnDownloadCallback() {
                @Override
                public void onSuccess(String path) {
                    Log.e("zzzurlPath",path);
                }

                @Override
                public void onFailed() {

                }
            });
        }
    }

    //检索所有下载链接
    private static Set<String> getResourceUrl(String htmlPath) {
        if (TextUtils.isEmpty(htmlPath)) {
            return null;
        }
        String htmlText = IOUtil.readPath(htmlPath);
        Set<String> resources = new HashSet<>();
        for (Pattern p : REGEX) {
            match(htmlText, p, resources);
        }
        return resources;
    }

    private static void match(String html, Pattern p, Set<String> result) {
        Matcher m = p.matcher(html);
        while (m.find()) {
            String s = m.group(1);
            if (s == null) continue;
            s = s.trim();
            try {
                if(s.contains("google")) continue;
                if (s.startsWith("data:")) continue;
                int i = s.indexOf('#');
                if (i != -1) s = s.substring(0, i);
                result.add(s);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("zzz","create url error", e);
            }
        }
    }

    public static WebResourceResponse shouldInterceptRequest(String webUrl,WebView view, String url){
        Log.e("zzzUrll2",url);
        try {
            String rootPath = view.getContext().getFilesDir()+ File.separator+Base64.MD5(webUrl);
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            FileInputStream input = new FileInputStream(rootPath + File.separator + fileName);
            return new WebResourceResponse(getMimeType(prefix), null, input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getMimeType(String prefix){
        switch (prefix){
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "js":
                return "application/x-javascript";
        }
        return null;
    }

}
