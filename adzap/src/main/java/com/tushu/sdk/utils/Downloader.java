package com.tushu.sdk.utils;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {

    private static Downloader mInstance;

    public static Downloader getInstance(){
        if (null == mInstance) {
            mInstance = new Downloader();
        }
        return mInstance;
    }

    public interface OnDownloadCallback{
        void onSuccess(String path);
        void onFailed();
    }

    public void download(final String url, final String path, final OnDownloadCallback onDownloadCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;//http连接对象
                BufferedInputStream bis = null;//缓冲输入流，从服务器获取
                RandomAccessFile raf = null;//随机读写器，用于写入文件，实现断点续传
                int len = 0;//每次读取的数组长度
                byte[] buffer = new byte[1024 * 8];//流读写的缓冲区
                try {
                    String filename = url.substring(url.lastIndexOf("/") + 1);
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    file = new File(path+File.separator+filename);
                    raf = new RandomAccessFile(file, "rwd");
                    conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setConnectTimeout(30*1000);//连接超时时间
                    conn.setReadTimeout(30*1000);//读取超时时间
                    conn.setRequestMethod("GET");//请求类型为GET
                    conn.connect();//连接
//                    int fileSize = conn.getContentLength();
                    bis = new BufferedInputStream(conn.getInputStream());//获取输入流并且包装为缓冲流
                    //从流读取字节数组到缓冲区
                    while (-1 != (len = bis.read(buffer))) {
                        //把字节数组写入到文件
                        raf.write(buffer, 0, len);
                    }
                    if (len == -1) {//如果读取到文件末尾则下载完成
                        if(null!=onDownloadCallback)onDownloadCallback.onSuccess(file.getPath());
                    } else {//否则下载系手动停止
                        if(null!=onDownloadCallback)onDownloadCallback.onFailed();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    if(null!=onDownloadCallback)onDownloadCallback.onFailed();
                }finally {
                    try{
                        if(null!=raf) raf.close();
                        if(null!=bis) bis.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }
}

