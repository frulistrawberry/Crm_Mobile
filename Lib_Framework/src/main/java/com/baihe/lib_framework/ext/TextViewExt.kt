package com.baihe.lib_framework.ext

import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.forEach
import androidx.core.view.marginEnd
import androidx.core.view.marginStart


fun TextView.getViewWidth(): Float {
    return paint.measureText(text.toString()) +
            paddingStart + paddingEnd + marginStart + marginEnd
}

/**
 * 给TextView添加删除线
 */
fun TextView.strikeThroughText() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

/**
 * 给TextView取消删除线
 */
fun TextView.cancelStrikeThroughText() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}


/**
 * 设置字体加小粗
 */
fun TextView.bold() {
    paint.isFakeBoldText = true
}

/**
 * 设置字体加大粗
 */
fun TextView.Bold() {
    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
}
