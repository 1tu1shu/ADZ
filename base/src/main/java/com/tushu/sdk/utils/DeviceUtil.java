package com.tushu.sdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


@SuppressLint("DefaultLocale")
public class DeviceUtil {

    /**
     * 获取手机imei
     * 
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        return "";
    }

    // Android Id
    private static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //Serial Number
    private static String getSerialNum(){
        return Build.SERIAL;
    }

    /**
     * 获取手机imei的md5
     * 
     * @param context
     * @return
     */
    public static String getImeiMd5(Context context) {
        String imei = getImei(context);
//        String imeiMd5 = Md5Util.md5LowerCase(imei);
        return imei;
    }

    /**
     * 为了防止mid获取失败,在需要有用户系统的唯一标示时，请使用
     * 
     * @param context
     * @return
     */
    private static final String PRE_UID = "pre_uid";

    public static String getUid(Context context) {

        if (!TextUtils.isEmpty(SharedPref.getString(context, PRE_UID, ""))) {
            return SharedPref.getString(context, PRE_UID, "");
        }

        String imei = getAndroidId(context);
        String serialNum = getSerialNum();

        String wifiMac = "";
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                wifiMac = (wifiInfo == null) ? "" : wifiInfo.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String blueMac = "";
//        try {
//            BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
//            if (ba != null) {
//                blueMac = ba.getAddress();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String uidStr = "";

        if (TextUtils.isEmpty(imei)) {
            imei = getPhoneTagStr();
        }
        if(TextUtils.isEmpty(serialNum)){
            serialNum = getPhoneTagStr();
        }
        if (TextUtils.isEmpty(wifiMac)) {
            wifiMac = getPhoneTagStr();
        }
        if (TextUtils.isEmpty(blueMac)) {
            blueMac = getPhoneTagStr();
        }
        uidStr = imei +serialNum+ wifiMac + blueMac;

        if (!TextUtils.isEmpty(uidStr)) {
            uidStr = Base64.MD5(uidStr.getBytes());
            SharedPref.setString(context, PRE_UID, uidStr);
        }
        return uidStr;
    }

    /**
     * 手机系统值
     * 
     * @return
     */
    private static String getPhoneTagStr() {
        StringBuilder sb = new StringBuilder();
        sb.append(Build.BOARD.length() % 10).append(Build.BRAND.length() % 10)
                .append(Build.CPU_ABI.length() % 10).append(Build.DEVICE.length() % 10)
                .append(Build.DISPLAY.length() % 10).append(Build.HOST.length() % 10)
                .append(Build.ID.length() % 10).append(Build.MANUFACTURER.length() % 10)
                .append(Build.MODEL.length() % 10).append(Build.PRODUCT.length() % 10)
                .append(Build.TAGS.length() % 10).append(Build.TYPE.length() % 10)
                .append(Build.USER.length() % 10);
        return sb.toString();
    }

    /**
     * 获取手机的mid
     * 
     * @return
     */
    public static int getTemperatureSensor() {
        int temperature = 0;
        String str = "/sys/class/hwmon/hwmon0/device/temp1_input";
        String temp = ReadInfo(str);
        if (temp == null || temp.trim().equals("")) {
            str = "/sys/class/power_supply/battery/temp";
            temp = ReadInfo(str);
        }
        if (temp == null || temp.trim().equals("")) {
            str = "/sys/class/power_supply/battery/batt_temp";
            temp = ReadInfo(str);
        }
        if (temp != null && !temp.trim().equals("")) {
            try {
                temperature = Integer.parseInt(temp.trim());
                while (temperature > 100)
                    temperature /= 10;
            } catch (Exception e) {
            }
        }

        return temperature;
    }

    public static String ReadInfo(String path) {
        String result = "";

        StringBuffer sb = new StringBuffer();
        if (new File(path).exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(path)));
                String aLine;
                while ((aLine = br.readLine()) != null) {
                    sb.append(aLine + "\n");
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        result = sb.toString();
        return result;
    }


    public static String getToken(Context context){
        return SharedPref.getString(context,"firebase_token","");
//        FirebaseInstanceId.getInstance().getToken();
    }


    public static String getDeviceInfo() {
        return Build.BRAND+"_"+Build.MODEL+"_"+Build.VERSION.RELEASE;
    }

    /**
     * 获取客户端版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * 获取客户端版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionName;
    }

}
