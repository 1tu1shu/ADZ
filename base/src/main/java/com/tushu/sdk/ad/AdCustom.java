package com.tushu.sdk.ad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


/**
 * Created by A03 on 2018/4/11.
 */

public class AdCustom extends AdBase {

    public AdCustom(@NonNull Context context) {
        super(context);
    }

    public AdCustom(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayout() {
        return layout;
    }


}
