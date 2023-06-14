package com.baihe.lib_common.service

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

interface IHomeService:IProvider {
    fun toHome(context:Context)

    fun getHomeFragment():Fragment
}