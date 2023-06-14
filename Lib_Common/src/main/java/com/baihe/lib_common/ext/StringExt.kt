package com.baihe.lib_common.ext

import java.util.regex.Pattern

fun String.isPhone():Boolean{
    val regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$"
    return if (length != 11) {
        false
    } else {
        val p = Pattern.compile(regex)
        val m = p.matcher(this)
        m.matches()
    }
}