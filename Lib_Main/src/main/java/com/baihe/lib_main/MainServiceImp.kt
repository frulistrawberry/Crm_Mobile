package com.baihe.lib_main

import android.content.Context
import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.service.IMainService
@Route(path = RoutePath.MAIN_SERVICE_MAIN)
class MainServiceImp:IMainService {

    override fun toMain(context: Context) {
        context.startActivity(Intent(context,MainActivity::class.java))
    }
}