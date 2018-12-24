package com.tushu.sdk.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmManagerUtil {

    public static final long ONE_DAY = 60 * 60* 24 * 1000;

    public static void setAlarm(Context context){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("action_ad_push");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 999, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar ca = GregorianCalendar.getInstance();
        ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), 20, 0,10);
        long startTime = ca.getTimeInMillis();
        if(System.currentTimeMillis()>startTime){
            startTime += ONE_DAY;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP,startTime,pendingIntent);
        } else {
            am.set(AlarmManager.RTC_WAKEUP,startTime,pendingIntent);
        }
    }
}
