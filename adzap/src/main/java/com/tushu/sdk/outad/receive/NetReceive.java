package com.tushu.sdk.outad.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;

import com.tushu.sdk.AdUtil;
import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.outad.OutADDBHelper;
import com.tushu.sdk.outad.activity.WebGameActivity;
import com.tushu.sdk.outad.adrequest.AdManager;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

public class NetReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.isConnected()) {
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                            Logger.d("当前WiFi连接可用 ");
                            AdManager.getInstence().setHasNet(true);
                            int netCode = SharedPref.getInt(context, SharedPref.NET_STATE, 1);
                            if (netCode != 1) {
                                checkAdShow(context);
                            }
                            SharedPref.setInt(context, SharedPref.NET_STATE, 1);
                        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                            Logger.d("当前移动网络连接可用 ");
                            AdManager.getInstence().setHasNet(true);
                            int netCode = SharedPref.getInt(context, SharedPref.NET_STATE, 2);
                            if (netCode != 2) {
                                checkAdShow(context);
                            }
                            SharedPref.setInt(context, SharedPref.NET_STATE, 2);
                        }
                    } else {
                        Logger.d("当前没有网络连接，请确保你已经打开网络 ");
                        AdManager.getInstence().setHasNet(false);
                        SharedPref.setInt(context, SharedPref.NET_STATE, 0);
                    }
                } else {
                    Logger.d("当前没有网络连接，请确保你已经打开网络 ");
                    AdManager.getInstence().setHasNet(false);
                    SharedPref.setInt(context, SharedPref.NET_STATE, 0);
                }
            }
        }
    }

    private void checkAdShow(final Context context) {
        Logger.d("开始进入判断 ");
        DotUtil.sendEvent(DotUtil.OUT_AD_NETCHANGE_ACTION);

        String adID = SharedPref.getString(context, SharedPref.AD_FACEBOOK_ID);

        Long last_show_time = SharedPref.getLong(context, SharedPref.LAST_SHOW_TIME, 0);

        OutADDBHelper helper = new OutADDBHelper(context);
        int showNum = Integer.parseInt(helper.getShowNum());

        AdModel adModel = AdUtil.getAdModel(adID);

        Long time = SharedPref.getLong(context, SharedPref.SHOW_FIRST_TIME, 0);

        Long installTime = SharedPref.getLong(context, SharedPref.INSTALL_TIME, 0);

        Logger.d("有没有超过时间:" + (System.currentTimeMillis() - last_show_time > adModel.screenIntervalTime));
        Logger.d("有没有超过次数:" + (showNum < adModel.screenNum));
        Logger.d("是否打开外插:" + (adModel.screenOpen == 1));
        Logger.d("是否超过初始设置时间:" + (System.currentTimeMillis() - installTime > adModel.screenOpenTime));
        Logger.d("!AdManager.getInstence().getShowAd()):" + (!AdManager.getInstence().getShowAd()));

        if (!TextUtils.isEmpty(adID)
                && System.currentTimeMillis() - last_show_time > adModel.screenIntervalTime
                && showNum < adModel.screenNum
                && adModel.screenOpen == 1
                && installTime != 0
                && System.currentTimeMillis() - installTime > adModel.screenOpenTime
                && !AdManager.getInstence().getShowAd()) {

            DotUtil.sendEvent(DotUtil.OUT_AD_NETCHANGE_JUDGE);
            SharedPref.setLong(context, SharedPref.LAST_SHOW_TIME, System.currentTimeMillis());

            Logger.d("设置加载次数:" + showNum);
//            if(showNum%2==0) {
                AdManager.getInstence().loadAd(context);
//            }else{
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        AdManager.getInstence().loadGame(context);
//                    }
//                },3000);
//            }

            if (time == 0) {
                SharedPref.setLong(context, SharedPref.SHOW_FIRST_TIME, System.currentTimeMillis());
            }
        }

        if (time != 0 && System.currentTimeMillis() - time > 1000 * 60 * 60 * 24) {
            String num = helper.getShowNum();
            helper.deleteShowNum(num);
            helper.saveShowNum("0");
            SharedPref.setLong(context, SharedPref.SHOW_FIRST_TIME, System.currentTimeMillis());
        }
    }

}
