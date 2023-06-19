
package com.baihe.lib_framework.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.Dimension
import com.baihe.lib_framework.helper.AppHelper


object DpToPx {
    @JvmStatic
    fun dpToPx(dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpValue,
            AppHelper.getApplication().resources.displayMetrics
        )
    }

    @JvmStatic
    fun dpToPx(dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpValue.toFloat(),
            AppHelper.getApplication().resources.displayMetrics
        ).toInt()
    }

    @JvmStatic
    fun dpToPx(context: Context, @Dimension(unit = Dimension.DP) dp: Int): Float {
        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics)
    }

    /**
     * 所有字体均使用dp
     */
    @JvmStatic
    fun spToPx(spValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            spValue,
            AppHelper.getApplication().resources.displayMetrics
        )
    }

    @JvmStatic
    fun pxToSp(spValue: Float):Float{
        val metrics: DisplayMetrics = AppHelper.getApplication().resources.displayMetrics
        return spValue / metrics.scaledDensity
    }
}
