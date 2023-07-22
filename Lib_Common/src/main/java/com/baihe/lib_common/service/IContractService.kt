package com.baihe.lib_common.service

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.template.IProvider
import com.baihe.lib_common.entity.CustomerDetailEntity

interface IContractService :IProvider{
    fun toContractList(context: Context)

    fun toContractDetail(context: Context,id:String)

    fun toAddOrUpdateContract(act:Activity,orderId:String,contractId:String?)


    override fun init(context: Context?) {

    }
}