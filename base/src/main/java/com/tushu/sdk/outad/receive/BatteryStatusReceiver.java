package com.tushu.sdk.outad.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.text.TextUtils;

import com.tushu.sdk.AdUtil;
import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.outad.OutADDBHelper;
import com.tushu.sdk.outad.adrequest.AdManager;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

import static android.content.Intent.ACTION_POWER_CONNECTED;
import static android.content.Intent.ACTION_POWER_DISCONNECTED;

/**
 * Created by Administrator on 2017/3/1.
 */
public class BatteryStatusReceiver extends BroadcastReceiver {

    /**
     * 充电连接
     */
    public void batteryConnected() { }

    /**
     * 充电断开
     */
    public void batteryDisconnected() { }

    /**
     * 充电状态改变
     */
    public void batteryChange() { }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_POWER_CONNECTED)) {
            batteryConnected();
        } else if (intent.getAction().equals(ACTION_POWER_DISCONNECTED)) {
            batteryDisconnected();
        } else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            if (status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL) {//充电中

                int chargeCode = SharedPref.getInt(context, SharedPref.CHARGE_STATE, 0);
                if (chargeCode != 1) {
                    SharedPref.setInt(context, SharedPref.CHARGE_STATE, 1);
                    checkAdShow(context);
                }
            } else {//断开电源

                int chargeCode = SharedPref.getInt(context, SharedPref.CHARGE_STATE, 0);
                if (chargeCode != 2) {
                    SharedPref.setInt(context, SharedPref.CHARGE_STATE, 2);
                    checkAdShow(context);
                }
            }

            batteryChange();
        }

    }

    private void checkAdShow(Context context) {

        Logger.d("进入充电判断");

        DotUtil.sendEvent(DotUtil.OUT_AD_CHARGE_ACTION);

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

            DotUtil.sendEvent(DotUtil.OUT_AD_CHARGE_JUDGE);
            SharedPref.setLong(context, SharedPref.LAST_SHOW_TIME, System.currentTimeMillis());

            AdManager.getInstence().loadAd(context);

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
