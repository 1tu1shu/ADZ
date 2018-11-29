package com.tushu.sdk.ad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by A03 on 2018/4/11.
 */

public class AdBanner extends AdBase {

    private ImageView iv_close;

    public AdBanner(@NonNull Context context) {
        super(context);
    }

    public AdBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayout() {
        return com.tushu.sdk.R.layout.adz_banner;
    }

    @Override
    protected void initViews() {
    }
}
