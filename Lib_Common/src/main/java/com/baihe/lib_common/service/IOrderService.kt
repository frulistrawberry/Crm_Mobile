package com.baihe.lib_common.service

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IOrderService: IProvider {
    fun toOrderList(context: Context)

    fun toOrderDetail(context: Context,orderId:String)

    fun toAddOrder(context: Activity,type:Int)

    override fun init(context: Context?) {

    }
}