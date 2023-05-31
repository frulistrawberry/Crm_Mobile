package com.baihe.lib_framework.helper

import android.app.Application

/**
 * @author mingyan.su
 * @date   2023/3/2 16:10
 * @desc   提供应用环境
 */
object AppHelper {
    private lateinit var app: Application
    private var isDebug = false

    fun init(application: Application, isDebug: Boolean) {
        this.app = application
        this.isDebug = isDebug
    }

    /**
     * 获取全局应用
     */
    @JvmStatic
    fun getApplication() = app

    /**
     * 是否为debug环境
     */
    fun isDebug() = isDebug
}