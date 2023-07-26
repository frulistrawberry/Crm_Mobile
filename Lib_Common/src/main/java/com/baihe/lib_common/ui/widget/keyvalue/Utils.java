package com.baihe.lib_common.ui.widget.keyvalue;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class Utils {
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

}
