package com.baihe.lib_common.http.api

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.annotation.JSONField
import com.baihe.http.annotation.HttpIgnore
import com.baihe.http.config.IRequestApi
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.helper.AppHelper
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_framework.utils.DeviceInfoUtils
import com.tencent.android.tpush.XGPushConfig

abstract  class BaseApi : IRequestApi {
    /**
     * 用户id
     */
    @delegate:HttpIgnore
    val userId:String? by lazy {
        UserServiceProvider.getUserId()
    }

    /**
     * 请求来源
     */
    @JvmField
    @HttpIgnore
    val appId:String = "20"

    /**
     * 平台
     */
    @HttpIgnore
    val platform:String = "2201"

    /**
     * 设备类型
     */
    @HttpIgnore
    @JSONField(name = "device_type")
    val deviceType:String = "1"

    /**
     * 推送设备token
     */
    @delegate:HttpIgnore
    val deviceToken:String? by lazy {
        XGPushConfig.getToken(AppHelper.getApplication())
    }

    /**
     * 设备名称
     */
    @delegate:HttpIgnore
    val device:String by lazy{
        "${DeviceInfoUtils.phoneBrand}${DeviceInfoUtils.phoneModel}"
    }

    /**
     * 版本号
     */
    @delegate:HttpIgnore
    @delegate:JSONField(name = "apver")
    val appVersion:String by lazy {
        AppManager.getAppVersionName(AppHelper.getApplication())
    }

    @delegate:JSONField(serialize = false)
    val params: String? by lazy {
         JSON.toJSONString(this)
     }


}
