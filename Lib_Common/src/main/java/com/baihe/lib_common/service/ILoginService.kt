package com.baihe.lib_common.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 登录服务相关接口
 */
interface ILoginService : IProvider {

    /**
     * 跳转登录页
     * @param context
     */
    fun login(context: Context)


    /**
     * 登出
     * @param context
     * @param lifecycleOwner
     * @param observer
     */
    fun logout(
    context: Context,
    lifecycleOwner: LifecycleOwner
    )

    override fun init(context: Context?) {

    }
}