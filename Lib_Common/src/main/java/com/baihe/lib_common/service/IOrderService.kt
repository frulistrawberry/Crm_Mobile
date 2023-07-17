package com.baihe.lib_common.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IOrderService: IProvider {
    fun toOrderList(context: Context)

    fun toOrderDetail(context: Context,orderId:String)

    override fun init(context: Context?) {

    }
}