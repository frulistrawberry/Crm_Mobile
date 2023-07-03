package com.baihe.lib_opportunity.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.service.IOpportunityService
import com.baihe.lib_opportunity.ui.activity.OpportunityListActivity

@Route(path = RoutePath.OPPORTUNITY_SERVICE_OPPORTUNITY)
class OpportunityServiceImp:IOpportunityService {
    override fun createOrUpdateOpportunity(context: Context, oppoId: String?, customerId: String?) {
        TODO("Not yet implemented")
    }

    override fun toOpportunityDetail(context: Context, id: String) {
        TODO("Not yet implemented")
    }

    override fun toOpportunityList(context: Context) {
        OpportunityListActivity.start(context)
    }
}