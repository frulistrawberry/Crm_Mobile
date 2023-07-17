package com.baihe.lib_opportunity.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.entity.OpportunityListItemEntity
import com.baihe.lib_common.entity.ResultEntity
import com.baihe.lib_common.entity.TempleEntity
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_common.service.IOpportunityService
import com.baihe.lib_opportunity.api.OpportunityRepository
import com.baihe.lib_opportunity.ui.activity.AddOrUpdateOpportunityActivity
import com.baihe.lib_opportunity.ui.activity.OpportunityDetailActivity
import com.baihe.lib_opportunity.ui.activity.OpportunityListActivity

@Route(path = RoutePath.OPPORTUNITY_SERVICE_OPPORTUNITY)
class OpportunityServiceImp:IOpportunityService {
    override fun createOrUpdateOpportunity(context: Context, oppoId: String?, customerId: String?) {
        AddOrUpdateOpportunityActivity.start(context,oppoId,customerId)
    }

    override suspend fun addOpportunity(lifecycleOwner: LifecycleOwner,params: LinkedHashMap<String, Any?>): ResultEntity? {
        val repository = OpportunityRepository(lifecycleOwner)
        return repository.addOpportunity(params)
    }

    override suspend fun updateOpportunity(lifecycleOwner: LifecycleOwner,params: LinkedHashMap<String, Any?>): Any? {
        val repository = OpportunityRepository(lifecycleOwner)
        return repository.updateOpportunity(params)
    }

    override suspend fun getOppoTemple(lifecycleOwner: LifecycleOwner,oppoId: String?, customerId:String?): TempleEntity?{
        val repository = OpportunityRepository(lifecycleOwner)
        return repository.getOpportunityTemple(oppoId,customerId)
    }

    override suspend fun getOppoList(
        lifecycleOwner: LifecycleOwner,
        page: Int,
        name: String?
    ): ListData<OpportunityListItemEntity>? {
        val repository = OpportunityRepository(lifecycleOwner)
        return repository.opportunityList(page,name=name)
    }


    override fun toOpportunityDetail(context: Context, id: String) {
        OpportunityDetailActivity.start(context,id)
    }

    override fun toEditOppo(context: Context, id: String,customerId: String) {
        AddOrUpdateOpportunityActivity.start(context,id,customerId)
    }

    override fun toOpportunityList(context: Context) {
        OpportunityListActivity.start(context)
    }
}