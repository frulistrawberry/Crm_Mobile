package com.baihe.lib_login

import android.annotation.SuppressLint
import android.os.Bundle
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.baihe.lib_login.databinding.LoginActivityLoginResultBinding

class LoginResultActivity: BaseViewBindActivity<LoginActivityLoginResultBinding>() {
    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        val userId = UserServiceProvider.getUserId()
        mBinding.textView.text = "登录成功 用户id：${userId}"
    }
}