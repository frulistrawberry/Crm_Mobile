package com.baihe.lib_framework.utils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.baihe.lib_framework.helper.AppHelper

/**
 * 资源获取工具类
 */
fun getStringArrayFromResource(@ArrayRes arrayId: Int): Array<String> {
    return AppHelper.getApplication().resources.getStringArray(arrayId)
}

fun getStringFromResource(@StringRes stringId: Int): String {
    return AppHelper.getApplication().getString(stringId)
}

fun getStringFromResource(@StringRes stringId: Int, vararg formatArgs: Any?): String? {
    return AppHelper.getApplication().getString(stringId, *formatArgs)
}

fun getColorFromResource(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(AppHelper.getApplication(), colorRes)
}

fun getImageFromResource(@DrawableRes drawableRes: Int):Drawable?{
    return ContextCompat.getDrawable(AppHelper.getApplication(),drawableRes)
}

fun getDimensionFromResource(@DimenRes dimenRes: Int): Int {
    return AppHelper.getApplication().resources
            .getDimensionPixelSize(dimenRes)
}


fun getColorDrawable(@ColorRes colorRes: Int): ColorDrawable? {
    return ColorDrawable(
        ContextCompat.getColor(
            AppHelper.getApplication(),
            colorRes
        )
    )
}