package com.baihe.lib_common.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IMainService:IProvider {
    fun toMain(context: Context)

    override fun init(context: Context?) {

    }
}