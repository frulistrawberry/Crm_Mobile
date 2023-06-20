package com.baihe.lib_home.service

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.lib_common.constant.RoutePath.HOME_SERVICE_HOME
import com.baihe.lib_common.service.IHomeService
import com.baihe.lib_home.demo.MainActivity
import com.baihe.lib_home.home.HomeFragment

@Route(path = HOME_SERVICE_HOME)
class HomeServiceImp:IHomeService {
    override fun toHome(context: Context) {
        MainActivity.start(context)
    }

    override fun getHomeFragment(): Fragment {
        return HomeFragment()
    }

    override fun init(context: Context?) {

    }
}