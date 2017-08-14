package com.zhonghe.lib_base.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Date: 2017/8/7.
 * Author: whyang
 */
public class Utilm {
    private static WindowManager wm;

    public static int getScreenWidth(Context context) {
        if (wm == null) {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }


    /**
     * 显示Toast
     *
     * @param context
     * @param string
     * @param resId
     */
    public static void toast(Context context, String string, int resId) {
        if (resId > 0) {
            Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param text
     */
    public static void toast(Context context, String text) {
        toast(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param resId
     * @param duration
     */
    public static void toast(Context context, @StringRes int resId, int duration) {
        toast(context, context.getString(resId), duration);
    }

    /**
     * 获取屏幕宽
     * @param context
     * @return
     */
    public static int GetWindowWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        return width;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
