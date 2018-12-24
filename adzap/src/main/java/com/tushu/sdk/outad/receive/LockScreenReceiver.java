package com.tushu.sdk.outad.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tushu.sdk.AdUtil;
import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.outad.OutADDBHelper;
import com.tushu.sdk.outad.activity.WebGameActivity;
import com.tushu.sdk.outad.adrequest.AdManager;
import com.tushu.sdk.utils.DotUtil;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

/**
 * 锁屏监听
 */

public class LockScreenReceiver extends BroadcastReceiver {

    /**
     * 屏幕关闭
     */
    public void screenOFF(Context context) { }

    /**
     * 屏幕亮起
     */
    public void screenON(Context context) {
    }

    /**
     * 屏幕解锁
     */
    public void screenPresent(Context context) { }

    @Override
    public void onReceive(Context context, Intent intent) {
        String mAction = intent.getAction();

        if (TextUtils.equals(mAction, Intent.ACTION_SCREEN_OFF)) {
            //关闭锁屏
            Logger.d("ACTION_SCREEN_OFF");
            AdManager.getInstence().setHasUnLock(false);
            screenOFF(context);
        } else if (mAction.equals(Intent.ACTION_SCREEN_ON)) {
            //开屏
            Logger.d("ACTION_SCREEN_ON");
            screenON(context);
        } else if (mAction.equals(Intent.ACTION_USER_PRESENT)) {
            //解锁
            Logger.d("ACTION_USER_PRESENT");
            AdManager.getInstence().setHasUnLock(true);
            outAd(context);
            screenPresent(context);
            AdManager.getInstence().loadGame(context);
        }
    }

    private void outAd(Context context) {

        DotUtil.sendEvent(DotUtil.OUT_AD_SCREENLOCK_ACTION);

        //弹广告
        String adID = SharedPref.getString(context, SharedPref.AD_FACEBOOK_ID);

        long last_show_time = SharedPref.getLong(context, SharedPref.LAST_SHOW_TIME, 0);

        OutADDBHelper helper = new OutADDBHelper(context);
        int showNum = Integer.parseInt(helper.getShowNum());

        AdModel adModel = AdUtil.getAdModel(adID);
        Logger.d(adModel.toString());

        long time = SharedPref.getLong(context, SharedPref.SHOW_FIRST_TIME, 0);

        long installTime = SharedPref.getLong(context, SharedPref.INSTALL_TIME, 0);

        Logger.d("是否过了广告间隔时间" + (System.currentTimeMillis() - last_show_time > adModel.screenIntervalTime));
        Logger.d("上次加载广告时间" + last_show_time);
        Logger.d("现在时间:" + System.currentTimeMillis());
        Logger.d("设置间隔时间:" + adModel.screenIntervalTime);
        Logger.d("-------------------------------------------------");
        Logger.d("是否超过最大次数 = " + (showNum <= adModel.screenNum));
        Logger.d("目前为止加载次数:" + showNum + "----- 设置最大次数:" + adModel.screenNum);
        Logger.d("-------------------------------------------------");
        Logger.d("是否打开开关 = " + (adModel.screenOpen == 1));
        Logger.d("-------------------------------------------------");
        Logger.d("安装时间是否为0 = " + (installTime != 0));
        Logger.d("-------------------------------------------------");
        Logger.d("是否超过第一次加载广告规定时间 = " + (System.currentTimeMillis() - installTime > adModel.screenOpenTime));
        Logger.d("现在时间 = " + System.currentTimeMillis());
        Logger.d("规定第一次广告加载时间 = " + adModel.screenOpenTime);
        Logger.d("安装时间 = " + installTime);
        Logger.d("-------------------------------------------------");
        Logger.d("AdManager.getInstence().getShowAd() = " + AdManager.getInstence().getShowAd());
        Logger.d("-------------------------------------------------");

        if (!TextUtils.isEmpty(adID)
                && System.currentTimeMillis() - last_show_time > adModel.screenIntervalTime
                && showNum < adModel.screenNum
                && adModel.screenOpen == 1
                && installTime != 0
                && System.currentTimeMillis() - installTime > adModel.screenOpenTime
                && !AdManager.getInstence().getShowAd()) {

            DotUtil.sendEvent(DotUtil.OUT_AD_SCREENLOCK_JUDGE);
            SharedPref.setLong(context, SharedPref.LAST_SHOW_TIME, System.currentTimeMillis());

            Logger.d("设置加载次数:" + showNum);
            //弹广告
            Logger.d("进入了判断，准备加载广告");

//            if(showNum%2==0) {
//                AdManager.getInstence().loadAd(context);
//            }else{
//                AdManager.getInstence().loadGame(context);
//            }

            if (time == 0) {
                Logger.d("设置第一次加载广告时间");
                SharedPref.setLong(context, SharedPref.SHOW_FIRST_TIME, System.currentTimeMillis());
            }
        }

        if (time != 0 && System.currentTimeMillis() - time > 1000 * 60 * 60 * 24) {
            Logger.d("一天过去了，次数清0");
            String num = helper.getShowNum();
            helper.deleteShowNum(num);
            helper.saveShowNum("0");
            SharedPref.setLong(context, SharedPref.SHOW_FIRST_TIME, System.currentTimeMillis());
        }
    }

}
