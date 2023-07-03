package com.baihe.lib_common.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IOpportunityService : IProvider {

    fun toOpportunityList(context:Context)

    fun toOpportunityDetail(context: Context,id:String)

    fun createOrUpdateOpportunity(context: Context,oppoId:String?=null,customerId:String?=null)

    override fun init(context: Context?) {

    }
}