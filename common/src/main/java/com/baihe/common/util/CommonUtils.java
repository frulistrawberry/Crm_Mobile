package com.baihe.common.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.List;
import java.util.UUID;

/**
 * Author：xubo
 * Time：2020-02-27
 * Description：
 */
public class CommonUtils {

    /**
     * dp转化成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static float dp2px(Context context, float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * dp转化成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2pxForInt(Context context, float dpValue) {
        return (int) (dp2px(context, dpValue) + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static float pxTosp(Context context, float pxValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return pxValue / metrics.scaledDensity;
    }

    /**
     * dp转化成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2pxForIntOffset(Context context, float dpValue) {
        return (int) dp2px(context, dpValue);
    }

    /**
     * sp转化成px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转化成px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2pxForInt(Context context, float spValue) {
        return (int) (sp2px(context, spValue) + 0.5f);
    }

    /**
     * sp转化成px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2pxForIntOffset(Context context, float spValue) {
        return (int) sp2px(context, spValue);
    }


    /**
     * 判断程序是否在前台
     *
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文本的宽度
     *
     * @return
     */
    public static int getTextWidth(String text, Paint paint) {
        int textWidth = 0;
        for (int index = 0; index < text.length(); index++) {
            char ch = text.charAt(index);
            float[] widths = new float[1];
            String srt = String.valueOf(ch);
            paint.getTextWidths(srt, widths);
            textWidth += widths[0];
        }
        return textWidth;
    }

    /**
     * 获取文本的高度
     *
     * @return
     */
    public static float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.bottom - fontMetrics.top;
    }

    /**
     * 获取文本的高度(int)
     *
     * @return
     */
    public static int getTextHeightForInt(Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return fontMetrics.bottom - fontMetrics.top;
    }

    /**
     * 获取文本偏移量
     *
     * @return
     */
    public static float getTextOffset(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return -fontMetrics.top;
    }

    /**
     * 获取屏幕宽
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕宽高
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 颜色转换
     *
     * @param startColor 起始颜色
     * @param endColor   终结颜色
     * @param value      转换比例
     * @return
     */
    public static int transformColor(int startColor, int endColor, float value) {
        int startAlpha = Color.alpha(startColor);
        int startRed = Color.red(startColor);
        int startGreen = Color.green(startColor);
        int startBlue = Color.blue(startColor);
        int endAlpha = Color.alpha(endColor);
        int endRed = Color.red(endColor);
        int endGreen = Color.green(endColor);
        int endBlue = Color.blue(endColor);
        int alpha = (int) (startAlpha + (endAlpha - startAlpha) * value);
        int red = (int) (startRed + (endRed - startRed) * value);
        int green = (int) (startGreen + (endGreen - startGreen) * value);
        int blue = (int) (startBlue + (endBlue - startBlue) * value);
        return Color.argb(alpha, red, green, blue);
    }
}
