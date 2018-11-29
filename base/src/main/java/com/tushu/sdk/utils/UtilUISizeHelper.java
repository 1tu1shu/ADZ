package com.tushu.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author Administrator
 *
 */
public final class UtilUISizeHelper {

    /**
     * 获取状态栏高度
     * 
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
//        int statusHeight = 0;
//        Rect localRect = new Rect();
//        view.getWindowVisibleDisplayFrame(localRect);
//        statusHeight = localRect.top;
//        if (0 == statusHeight){
//            Class<?> localClass;
//            try {
//                localClass = Class.forName("com.android.internal.R$dimen");
//                Object localObject = localClass.newInstance();
//                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
//                statusHeight = context.getResources().getDimensionPixelSize(i5);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (statusHeight == 0) {
//            statusHeight = dpTopx(context, 25);
//        }
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取屏幕高度
     * 
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getHeight();
    }

    /**
     * 获取屏幕宽度
     * 
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getWidth();
    }

    public static int dpTopx(Context context, float fDpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (fDpValue * scale + 0.5f);
    }

    public static int pxTodp(Context context, float fPxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (fPxValue / scale + 0.5f);
    }

    public static float pxTosp(Context context, float fPxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return fPxValue / scale + 0.5f;
    }

    public static float spTopx(Context context, float fSpValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return fSpValue * scale + 0.5f;
    }

    public static DisplayMetrics getDisplayMetrics(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(dm);
        return dm;
    }
}
