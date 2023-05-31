package com.baihe.lib_framework.interfaces

import android.app.Activity

/**
 * App状态监听
 */
interface AppFrontBackListener {
    /**
     * 前台
     */
    fun onFront(activity: Activity?)

    /**
     * 后台
     */
    fun onBack(activity: Activity?)
}