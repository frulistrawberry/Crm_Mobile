package com.baihe.lib_common

import android.app.Application
import com.baihe.http.EasyConfig
import com.baihe.imageloader.ImageLoaderConfig
import com.baihe.imageloader.ImageLoaderUtils
import com.baihe.lib_common.http.RequestHandler
import com.baihe.lib_common.http.cookie.CookiesInterceptor
import com.baihe.lib_common.http.cookie.HeaderInterceptor
import com.baihe.lib_common.http.server.ApiServer
import com.baihe.lib_framework.helper.AppHelper
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.manager.AppManager
import com.safframework.log.okhttp.LoggingInterceptor
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object CommonConfig {

    @JvmStatic
    fun init(context: Application,isDebug:Boolean){
        AppHelper.init(context,isDebug)
        AppManager.init(context)
        LogUtil.init(isDebug)
        initMmkv(isDebug)
        initHttp(isDebug)
        initImageLoader()

    }

    private fun initMmkv(isDebug: Boolean){
        val rootDir: String = MMKV.initialize(AppHelper.getApplication())
        MMKV.setLogLevel(
            if (isDebug) {
                MMKVLogLevel.LevelDebug
            } else {
                MMKVLogLevel.LevelError
            }
        )
        LogUtil.d(tag = "MMKV","mmkv root: $rootDir")
    }

    private fun initHttp(isDebug: Boolean){
        val apiServer = ApiServer()
        val okHttpClient = OkHttpClient.Builder().apply {
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            addInterceptor(CookiesInterceptor())
            addInterceptor(HeaderInterceptor())
            addInterceptor(LoggingInterceptor
                .Builder().apply {
                    this.isDebug = isDebug
                    TAG = "OKHttp"

            }.build())
        }.build()

        EasyConfig.with(okHttpClient).apply {
            server = apiServer
            handler = RequestHandler()
            retryCount = 1
            retryTime = 2000
            isLogEnabled = false
        }.into()
    }

    private fun initImageLoader(){
        val config = ImageLoaderConfig.Builder().apply {
            placePicRes(R.drawable.common_image_load_default)
            errorPicRes(R.drawable.common_image_load_default)
            setMaxDiskCache(1024 * 1024 * 50)
            setMaxMemoryCache(1024 * 1024 * 10)
        }.build()
        ImageLoaderUtils.init(config)
    }

}