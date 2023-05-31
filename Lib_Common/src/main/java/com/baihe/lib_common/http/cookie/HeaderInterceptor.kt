package com.baihe.lib_common.http.cookie

import com.baihe.lib_framework.log.LogUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 添加头信息
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()
        newBuilder.addHeader("Content-type", "application/json; charset=utf-8")
        val cookies = CookiesManager.getCookies()
        LogUtil.e("Cookies","HeaderInterceptor:cookies:$cookies")
        if (!cookies.isNullOrEmpty()) {
                newBuilder.addHeader("Cookie", cookies)
        }
        return chain.proceed(newBuilder.build())
    }
}