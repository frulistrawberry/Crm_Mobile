package com.baihe.lib_common.http.api

import com.baihe.http.annotation.HttpHeader
import com.baihe.http.annotation.HttpIgnore
import com.baihe.http.config.IRequestApi
import com.baihe.lib_common.http.cookie.CookieJarImpl
import okhttp3.Cookie

data class CommonApi(
    @HttpIgnore
    val path:String,
    val params:String? = null
):IRequestApi {


    override fun getApi(): String {
        return path
    }
}