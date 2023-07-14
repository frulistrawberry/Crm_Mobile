package com.baihe.lib_user.utils

object UserRegexUtil {
    fun mobileEncrypt(mobile: String): String {
        return if (mobile.isEmpty() || mobile.length != 11) {
            mobile
        } else {
            mobile.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        }
    }

    fun versionEncrypt(versionName: String?): Int? {
        val version = versionName?.replace("[.]".toRegex(), "")
        return version?.toInt()
    }
}