package com.baihe.lihepro

import android.os.Bundle
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.baihe.lihepro.databinding.ActivityMainBinding

class MainActivity : BaseViewBindActivity<ActivityMainBinding>() {



    override fun initView(savedInstanceState: Bundle?) {
       mBinding.textView.text = UserServiceProvider.getUserId()
    }


}