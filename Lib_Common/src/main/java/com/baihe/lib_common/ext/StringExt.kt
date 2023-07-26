package com.baihe.lib_common.ext

import java.util.regex.Pattern


object StringExt{
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

    fun String.isPassword():Boolean{
        val regex = "^((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])|(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()-_=+\\[{\\]}\\|;:'\",<.>/?])|(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*()-_=+\\[{\\]}\\|;:'\",<.>/?])|(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()-_=+\\[{\\]}\\|;:'\",<.>/?])).{8,}$"
        return if (length<8||length>15){
            false
        }else{
            val p = Pattern.compile(regex)
            val m = p.matcher(this)
            m.matches()
        }

    }
}
