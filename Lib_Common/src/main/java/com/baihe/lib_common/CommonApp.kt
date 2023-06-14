package com.baihe.lib_common

import android.app.Application

class CommonApp:Application() {

    override fun onCreate() {
        super.onCreate()
        CommonConfig.init(this,BuildConfig.DEBUG)
    }
}