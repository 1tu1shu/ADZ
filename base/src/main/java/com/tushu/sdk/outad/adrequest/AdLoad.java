package com.tushu.sdk.outad.adrequest;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

public interface AdLoad {

    void loadAd(Context context, String adID);

    void loadAdView(Context context);

    void addViewToActivity(Activity activity, ViewGroup viewGroup);
}
