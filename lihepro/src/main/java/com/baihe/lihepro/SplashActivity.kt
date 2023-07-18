package com.baihe.lihepro

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.provider.LoginServiceProvider
import com.baihe.lib_common.provider.MainServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.baihe.lib_framework.ext.FlowExt
import com.baihe.lib_framework.ext.FlowExt.countDownCoroutines
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lihepro.databinding.ActivitySplashBinding

class SplashActivity:BaseViewBindActivity<ActivitySplashBinding>() {


    override fun initView(savedInstanceState: Bundle?) {

        countDownCoroutines(2, lifecycleScope, onTick = {

        }) {
            if (!UserServiceProvider.getUserId().isNullOrEmpty()){
                MainServiceProvider.toMain(this)
            }else{
                LoginServiceProvider.login(this)
            }
            finish()
        }


    }

}