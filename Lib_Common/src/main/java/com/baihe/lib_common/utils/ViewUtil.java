package com.baihe.lib_common.utils;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.core.graphics.drawable.DrawableCompat;

import com.baihe.lib_framework.helper.AppHelper;

public class ViewUtil {
    /**
     * 图表渐变
     * @param imageView
     * @param imageResId
     * @param percent
     * @param formColor
     * @param toColor
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getIcon(int imageResId, float percent, int formColor, int toColor){
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int color = (int) argbEvaluator.evaluate(percent, formColor, toColor);
        Drawable wrapDrawable = DrawableCompat.wrap(AppHelper.getApplication().getDrawable(imageResId));
        DrawableCompat.setTint(wrapDrawable, color);
        return wrapDrawable;
    }

    public static int getColor(float percent, int formColor, int toColor){
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        return (int) argbEvaluator.evaluate(percent, formColor, toColor);
    }
}
