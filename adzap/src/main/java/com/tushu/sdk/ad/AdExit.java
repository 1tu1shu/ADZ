package com.tushu.sdk.ad;

import android.content.Context;
import android.util.AttributeSet;


/**
 * Created by A03 on 2018/4/9.
 */

public class AdExit extends AdBase {

    public AdExit(Context context) {
        super(context);
    }

    public AdExit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayout() {
        return com.tushu.sdk.R.layout.adz_exit;
    }

}
