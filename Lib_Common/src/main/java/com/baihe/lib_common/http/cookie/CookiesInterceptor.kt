package com.baihe.lib_common.http.cookie

import com.baihe.lib_framework.log.LogUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc   Cookies拦截器
 */
class CookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newBuilder = request.newBuilder()

        val response = chain.proceed(newBuilder.build())

        // set-cookie maybe has multi, login to save cookie
        val cookies = response.headers("set-cookie")
        val cookiesStr = CookiesManager.encodeCookie(cookies)
        CookiesManager.saveCookies(cookiesStr)
        LogUtil.e("Cookies","CookiesInterceptor:cookies:$cookies")
        return response
    }
}