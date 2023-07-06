package com.baihe.lib_common.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface ICustomerService :IProvider{
    fun toCustomerList(context: Context)

    fun toCustomerDetail(context: Context,id:String)

    fun createOrUpdateCustomer(context: Context,customerId:String?=null)

    fun chooseCustomer(context: Context)

    override fun init(context: Context?) {

    }
}