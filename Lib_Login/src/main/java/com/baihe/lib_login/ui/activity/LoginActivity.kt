package com.baihe.lib_login.ui.activity

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_login.R

class LoginActivity: BaseActivity() {

    companion object{
        fun start(context: Context){
            val starter = Intent(context, LoginActivity::class.java)
            if (context is Application) {
                starter.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(starter)
                return
            }
            if (context is Activity){
                context.startActivityForResult(starter, RequestCode.REQUEST_LOGIN)
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.login_activity_login
    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}