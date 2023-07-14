package com.baihe.lihepro

import android.os.Bundle
import com.baihe.lib_common.provider.HomeServiceProvider
import com.baihe.lib_common.provider.MessageServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.baihe.lib_user.ui.MineFragment
import com.baihe.lihepro.databinding.ActivityMainBinding

class MainActivity : BaseViewBindActivity<ActivityMainBinding>() {



    override fun initView(savedInstanceState: Bundle?) {
       supportFragmentManager.beginTransaction().replace(R.id.container, HomeServiceProvider.getHomeFragment()).commit()
    }


}