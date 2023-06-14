package com.baihe.lib_common.provider

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.HOME_SERVICE_HOME
import com.baihe.lib_common.service.IHomeService

object HomeServiceProvider {
    @Autowired(name = HOME_SERVICE_HOME)
    lateinit var homeService: IHomeService

    init {
        ARouter.getInstance().inject(this)
    }

    @JvmStatic
    fun toHome(context: Context){
        homeService.toHome(context)
    }

    @JvmStatic
    fun getHomeFragment():Fragment{
        return homeService.getHomeFragment()
    }
}