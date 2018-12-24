package com.tushu.sdk.ad;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tushu.sdk.AdUtil;
import com.tushu.sdk.TSSDK;
import com.tushu.sdk.utils.SharedPref;

public class AdProxy {

    private static AdProxy mInstance;
    public static AdProxy getInstance(){
        if(null==mInstance) {
            mInstance = new AdProxy();
        }
        return mInstance;
    }

    public interface OnTypeCallback{
        void loadFacebook(String adFbId);
        void loadGoogle();
        void loadADT();
    }

    public void loadAd(Context context,String adFbId,OnTypeCallback callback){
        if(TSSDK.isVIP) return;

        if(null!=adFbId){
            int requestNum = SharedPref.getIntPrivate(context,"requestNum"+adFbId,0,"adz_preferences");
            String[] priorityArray = AdUtil.getAdModel(adFbId).priorityArray;
            if(null!=priorityArray){
                String platform = priorityArray[requestNum%priorityArray.length];
//                Log.e("zzz",adFbId+"请求"+platform);
                switch (platform){
                    case "facebook":
                        String adFbId2 = AdUtil.getAdModel(adFbId).screenPlacementId;
                        if (!TextUtils.isEmpty(adFbId2)) {
//                            Log.e("zzz", "Facebook拿到网络ID" + adFbId2);
                            adFbId = adFbId2;
                        }
                        callback.loadFacebook(adFbId);
                        break;
                    case "admob":
                        callback.loadGoogle();
                        break;
                    case "adt":
                        callback.loadADT();
                        break;
                }
                SharedPref.setIntPrivate(context,"requestNum"+adFbId,++requestNum,"adz_preferences");
            }else{
                callback.loadFacebook(adFbId);
            }
        }
    }


}