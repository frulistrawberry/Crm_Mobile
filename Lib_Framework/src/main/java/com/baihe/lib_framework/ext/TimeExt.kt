package com.baihe.lib_framework.ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object TimeExt{
    //毫秒的long时间转化
    fun Long.getDay(): String = (this / (1000*3600 * 24)).asTwoDigit()
    fun Long.getHour(): String = (this % (1000*3600 * 24) / 3600).asTwoDigit()
    fun Long.getMin(): String = (this % (1000*3600 * 24) % 3600 / 60).asTwoDigit()
    fun Long.getSec(): String = (this % (1000*3600 * 24) % 3600 % 60).asTwoDigit()

    fun Long.formattedTime(keepHours: Boolean = false): String {
        System.currentTimeMillis()
        val seconds = this / 1000
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = (seconds % 3600) % 60

        return if (!keepHours && h == 0L) {
            "${m.asTwoDigit()}:${s.asTwoDigit()}"
        } else {
            "${h.asTwoDigit()}:${m.asTwoDigit()}:${s.asTwoDigit()}"
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun Long.formattedDateTime():String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return sdf.format(this)
    }

    @SuppressLint("SimpleDateFormat")
    fun Long.formattedDate():String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(this)
    }

    @SuppressLint("SimpleDateFormat")
    fun String.toMillis():Long{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(this)
        return date.time
    }

    fun Long.asTwoDigit(): String {
        val value = StringBuilder()
        if (this < 10) {
            value.append("0")
        }
        value.append(toString())
        return value.toString()
    }
}
