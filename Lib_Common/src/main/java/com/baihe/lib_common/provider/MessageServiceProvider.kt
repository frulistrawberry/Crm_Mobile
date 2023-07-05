package com.baihe.lib_common.provider

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath.HOME_SERVICE_MESSAGE
import com.baihe.lib_common.service.IMessageService

/**
 * @author xukankan
 * @date 2023/7/5 11:02
 * @email：xukankan@jiayuan.com
 * @description：
 */
object MessageServiceProvider {
    @Autowired(name = HOME_SERVICE_MESSAGE)
    lateinit var messageService: IMessageService

    init {
        ARouter.getInstance().inject(this)
    }

    @JvmStatic
    fun toMessage(context: Context) {
        messageService.toMessage(context)
    }

    @JvmStatic
    fun getMessageFragment(): Fragment {
        return messageService.getMessageFragment()
    }
}