package com.tushu.sdk.outad.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.tushu.sdk.AdUtil;
import com.tushu.sdk.utils.AlarmManagerUtil;

public class AdReceiver extends BroadcastReceiver {

    private boolean isShow = false;
    private Context context;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            isShow = false;
            AlarmManagerUtil.setAlarm(context);
        }
    };

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        if(null!=intent.getAction()&&intent.getAction().equals("action_ad_push")){
            //刷新广告配置
            if(!isShow) {
                isShow = true;
                AdUtil.getServerData(context);
            }
            handler.sendEmptyMessageDelayed(110,30*1000);

        }
    }

}