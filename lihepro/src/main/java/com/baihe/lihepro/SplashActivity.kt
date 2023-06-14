package com.baihe.lihepro

import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.provider.LoginServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseActivity

class SplashActivity:BaseActivity() {
    override fun getLayoutResId(): Int {
        return -1
    }

    override fun setContentLayout() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        if (!UserServiceProvider.getUserId().isNullOrEmpty()){
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            LoginServiceProvider.login(this)
        }
        finish()
    }
}