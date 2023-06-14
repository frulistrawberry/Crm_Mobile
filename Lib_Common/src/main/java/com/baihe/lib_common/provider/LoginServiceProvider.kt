package com.baihe.lib_common.provider

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.LOGIN_SERVICE_LOGIN
import com.baihe.lib_common.service.ILoginService

object LoginServiceProvider  {
    @Autowired(name = LOGIN_SERVICE_LOGIN)
    lateinit var loginService: ILoginService
    init {
        ARouter.getInstance().inject(this)
    }

    fun logout(context: Context, lifecycleOwner: LifecycleOwner){
        loginService.logout(context,lifecycleOwner)
    }

    fun login(context: Context){
        loginService.login(context)
    }
}