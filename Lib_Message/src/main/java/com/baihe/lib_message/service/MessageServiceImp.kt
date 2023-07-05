package com.baihe.lib_message.service

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.service.IMessageService
import com.baihe.lib_message.ui.fragment.MessageFragment

/**
 * @author xukankan
 * @date 2023/7/5 11:16
 * @email：xukankan@jiayuan.com
 * @description：
 */
@Route(path = RoutePath.HOME_SERVICE_MESSAGE)
class MessageServiceImp : IMessageService {
    override fun getMessageFragment(): Fragment {
        return MessageFragment()
    }

    override fun init(context: Context?) {

    }

    override fun toMessage(context: Context) {

    }

}