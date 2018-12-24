package com.tushu.sdk.utils;

import android.util.Log;

public class Logger {

    private static final String TAG = "zzz";
    private static boolean showLog = true;

    public static void d(String content) {
        if (showLog) {
            Log.e(TAG, content);
        }
    }

    public static void e(String content) {
        if (showLog) {
            Log.e(TAG, content);
        }
    }

    public static void i(String content) {
        if (showLog) {
            Log.i(TAG, content);
        }
    }

}
