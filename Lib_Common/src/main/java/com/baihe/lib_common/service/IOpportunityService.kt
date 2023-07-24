package com.baihe.lib_common.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.template.IProvider
import com.baihe.lib_common.entity.OpportunityListItemEntity
import com.baihe.lib_common.entity.ResultEntity
import com.baihe.lib_common.entity.TempleEntity
import com.baihe.lib_common.http.response.ListData

interface IOpportunityService : IProvider {

    fun toOpportunityList(context:Context)

    fun toOpportunityDetail(context: Context,id:String)
    fun toEditOppo(context: Context,reqId:String,customerId: String,title:String?=null)

    fun createOrUpdateOpportunity(context: Context,oppoId:String?=null,customerId:String?=null,title:String?=null)
    suspend fun addOpportunity(lifecycleOwner: LifecycleOwner,params:LinkedHashMap<String,Any?>): ResultEntity?

    suspend fun updateOpportunity(lifecycleOwner: LifecycleOwner,params:LinkedHashMap<String,Any?>):Any?
    suspend fun getOppoTemple(lifecycleOwner: LifecycleOwner, oppoId: String?=null, customerId:String?=null): TempleEntity?

    suspend fun getOppoList(lifecycleOwner: LifecycleOwner,page:Int,name:String?): ListData<OpportunityListItemEntity>?
    override fun init(context: Context?) {

    }
}