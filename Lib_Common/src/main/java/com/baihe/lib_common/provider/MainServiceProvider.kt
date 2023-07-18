package com.baihe.lib_common.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.service.IMainService

object MainServiceProvider {
    @Autowired(name = RoutePath.MAIN_SERVICE_MAIN)
    lateinit var service: IMainService

    init {
        ARouter.getInstance().inject(this)
    }

    @JvmStatic
    fun toMain(context: Context) = service.toMain(context)
}