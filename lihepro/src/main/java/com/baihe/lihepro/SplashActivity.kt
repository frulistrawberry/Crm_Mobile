package com.baihe.lihepro

import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.provider.LoginServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_customer.ui.activity.CustomerListActivity
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_home.waiting.WaitingListActivity

class SplashActivity:BaseActivity() {
    override fun getLayoutResId(): Int {
        return -1
    }

    override fun setContentLayout() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        if (!UserServiceProvider.getUserId().isNullOrEmpty()){
            val userId = UserServiceProvider.getUserId()
            LogUtil.d(TAG,userId)
            startActivity(Intent(this,MainActivity::class.java))
        }else{
            LoginServiceProvider.login(this)
        }
        finish()
    }
}