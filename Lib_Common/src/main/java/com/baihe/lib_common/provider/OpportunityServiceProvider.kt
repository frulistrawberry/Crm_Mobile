package com.baihe.lib_common.provider

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.entity.OpportunityListItemEntity
import com.baihe.lib_common.entity.ResultEntity
import com.baihe.lib_common.entity.TempleEntity
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_common.service.ICustomerService
import com.baihe.lib_common.service.IOpportunityService

object OpportunityServiceProvider {
    @Autowired(name = RoutePath.OPPORTUNITY_SERVICE_OPPORTUNITY)
    lateinit var service: IOpportunityService

    init {
        ARouter.getInstance().inject(this)
    }

    /**
     * 机会列表
     */
    @JvmStatic
    fun toOpportunityList(context: Context) = service.toOpportunityList(context)

    /**
     * 机会详情
     * @param id 机会Id
     */
    @JvmStatic
    fun toOpportunityDetail(context: Context, id:String) = service.toOpportunityDetail(context,id)

    /**
     * 编辑机会
     */
    fun toEditOppo(context: Context,reqId: String,customerId: String,title:String?=null) = service.toEditOppo(context,reqId,customerId,title)

    /**
     * 创建机会
     */
    @JvmStatic
    fun createOrUpdateOpportunity(context: Context,oppoId:String?=null, customerId:String?=null,title:String?=null) = service.createOrUpdateOpportunity(context,oppoId,customerId,title)

    @JvmStatic
    suspend fun addOpportunity(lifecycleOwner: LifecycleOwner, params:LinkedHashMap<String,Any?>):ResultEntity? = service.addOpportunity(lifecycleOwner,params)
    @JvmStatic
    suspend fun updateOpportunity(lifecycleOwner: LifecycleOwner, params:LinkedHashMap<String,Any?>):Any? = service.updateOpportunity(lifecycleOwner,params)
    @JvmStatic
    suspend fun getOppoList(lifecycleOwner: LifecycleOwner, page:Int = 0,name:String?=""): ListData<OpportunityListItemEntity>? = service.getOppoList(lifecycleOwner,page,name)
    @JvmStatic
    suspend fun getOppoTemple(lifecycleOwner: LifecycleOwner, oppoId: String?=null, customerId:String?=null): TempleEntity? = service.getOppoTemple(lifecycleOwner,oppoId,customerId)
}