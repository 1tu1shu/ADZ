package com.tushu.sdk;

import android.content.Context;
import android.text.TextUtils;

import com.tushu.sdk.ad.AdModel;
import com.tushu.sdk.outad.OutADDBHelper;
import com.tushu.sdk.net.AsyncTaskNew;
import com.tushu.sdk.net.HttpHelper;
import com.tushu.sdk.utils.Logger;
import com.tushu.sdk.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A03 on 2018/6/7.
 */

public class AdUtil {

    public static Map<String, AdModel> adMap = new HashMap<>();

    public static void getServerData(final Context context) {

        String jsonStr = SharedPref.getString(context, SharedPref.AD_SERVICE_CONFIG);
        if(!TextUtils.isEmpty(jsonStr)){
            parseJson(context,jsonStr);
        }

        new AsyncTaskNew<String>() {
            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                SharedPref.setString(context, SharedPref.AD_SERVICE_CONFIG, result);
                parseJson(context,result);
            }

            @Override
            protected String doInBackground(String... params) {
                return HttpHelper.doGet("http://s3.tusumobi.com/appPackage/" + context.getPackageName() + ".json");
            }
        }.execute();

    }


    public static AdModel getAdModel(String adId) {
        if (null == adMap.get(adId)) {
            return new AdModel();
        } else {
            return adMap.get(adId);
        }
    }

    private static void parseJson(Context context, String result) {
        try {
            JSONArray jsonArr = new JSONArray(result);
            Logger.d(result);
            JSONObject jsonObj = null;
            AdModel adModel = null;
            OutADDBHelper helper = new OutADDBHelper(context);
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonObj = jsonArr.optJSONObject(i);
                adModel = new AdModel();
                adModel.channelName = jsonObj.getString("channelName");
                adModel.adClickInvalid = jsonObj.optInt("adClickInvalid");
                adModel.backClickable = jsonObj.optInt("backClickable");
                adModel.bigImgClickable = jsonObj.optInt("bigImgClickable");
                adModel.closeBtnTime = jsonObj.optLong("closeBtnTime");
                adModel.adid = jsonObj.getString("adId");//ADT广告ID

                adModel.backBtnTime = jsonObj.optLong("backBtnTime");
                adModel.coverRate = jsonObj.optInt("coverRate");
                adModel.descClickable = jsonObj.optInt("descClickable");
                adModel.iconClickable = jsonObj.optInt("iconClickable");
                adModel.titleClickable = jsonObj.optInt("titleClickable");

                try {
                    adModel.screenPlacementId = jsonObj.getString("screenPlacementId");//广告ID
                    if (jsonObj.optInt("insertScreen") == 1) {
                        adModel.screenIntervalTime = jsonObj.getInt("screenIntervalTime") * 60 * 1000L;//广告间隔时间
                        adModel.screenNum = jsonObj.optInt("screenNum");//广告展示次数
                        adModel.screenOpen = jsonObj.optInt("screenOpen");//是否开启广告
                        adModel.screenOpenTime = jsonObj.getDouble("screenOpenTime") * 60 * 60 * 1000L;//广告开启时间
//                        adModel.screenOpenTime = Double.valueOf(jsonObj.getDouble("screenOpenTime")).longValue() * 60 * 60 * 1000L;//广告开启时间
                        if (TextUtils.equals(adModel.channelName, "facebook")) {
                            Logger.d("-----配置加载facebook广告");
                            helper.deleteShowType(helper.getShowType());
                            helper.saveShowType("facebook");
                            SharedPref.setString(context, SharedPref.AD_FACEBOOK_ID, adModel.screenPlacementId);
                            adMap.put(adModel.screenPlacementId, adModel);
                            Logger.d(adModel.toString());
                        } else if (TextUtils.equals(adModel.channelName, "adtiming")) {
                            Logger.d("-----配置加载adtiming广告");
                            helper.deleteShowType(helper.getShowType());
                            helper.saveShowType("adtiming");
//                            SharedPref.setString(context, SharedPref.AD_ADT_ID, adModel.adid);
                        } else if (TextUtils.equals(adModel.channelName, "admob")) {
                            Logger.d("-----配置加载admob广告");
                            helper.deleteShowType(helper.getShowType());
                            helper.saveShowType("admob");
//                            SharedPref.setString(context, SharedPref.AD_GOOGLE_ID, adModel.adid);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adMap.put(adModel.adid, adModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}