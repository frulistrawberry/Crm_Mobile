package com.baihe.lib_login.login.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_login.R

class LoginActivity: BaseActivity() {

    companion object{
        fun start(context: Context){
            val starter = Intent(context,LoginActivity::class.java)
            if (context is Application) {
                starter.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            context.startActivity(starter)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.login_activity_login
    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}