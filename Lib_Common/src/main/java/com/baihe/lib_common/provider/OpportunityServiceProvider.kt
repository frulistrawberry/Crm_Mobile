package com.baihe.lib_common.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath
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
     */
    @JvmStatic
    fun toOpportunityDetail(context: Context, id:String) = service.toOpportunityDetail(context,id)

    /**
     * 创建机会
     */
    @JvmStatic
    fun createOrUpdateOpportunity(context: Context,oppoId:String?, customerId:String?=null) = service.createOrUpdateOpportunity(context,oppoId,customerId)
}