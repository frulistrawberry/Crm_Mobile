package com.baihe.lib_common.service

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author xukankan
 * @date 2023/7/5 11:03
 * @email：xukankan@jiayuan.com
 * @description：
 */
interface IMessageService : IProvider {
    /**
     * 去主页-消息页面
     * @param context
     */
    fun toMessage(context: Context)

    /**
     * 获取主页-消息页面
     */
    fun getMessageFragment(): Fragment
}