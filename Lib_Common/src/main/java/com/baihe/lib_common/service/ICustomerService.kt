package com.baihe.lib_common.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.template.IProvider
import com.baihe.lib_common.entity.CustomerDetailEntity

interface ICustomerService :IProvider{
    fun toCustomerList(context: Context)

    fun toCustomerDetail(context: Context,id:String)

    fun createOrUpdateCustomer(context: Context,customerId:String?=null)

    fun chooseCustomer(context: Context)


    suspend fun getCustomerInfo(customerId: String,lifecycleOwner: LifecycleOwner):CustomerDetailEntity?

    override fun init(context: Context?) {

    }
}