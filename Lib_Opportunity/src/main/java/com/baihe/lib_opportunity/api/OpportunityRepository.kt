package com.baihe.lib_opportunity.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.entity.TempleEntity
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.Data
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValEventEntity
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_opportunity.OpportunityDetailEntity
import com.baihe.lib_opportunity.OpportunityListItemEntity
import com.baihe.lib_opportunity.constant.UrlConstant

class OpportunityRepository(lifecycle: LifecycleOwner): BaseRepository(lifecycle) {
    suspend fun opportunityList(page:Int,reqPhase:String?="0",name:String?,filter:LinkedHashMap<String,Any?>?,pageSize:Int = 10): ListData<OpportunityListItemEntity>? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("page",page)
                .putParamValue("name",name)
                .putParamValue("reqPhase",reqPhase)
                .putParamValue("pageSize",pageSize)
                .putParamValue(filter)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.OPPORTUNITY_LIST,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ListData<OpportunityListItemEntity>>>() {})
        }
    }

    suspend fun opportunityDetail(oppoId:String):OpportunityDetailEntity? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("reqId",oppoId)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.OPPORTUNITY_DETAIL,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<OpportunityDetailEntity>>() {})
        }
    }

    suspend fun getOpportunityTemple(oppoId: String?):TempleEntity?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
            if (!oppoId.isNullOrEmpty())
                jsonParam.putParamValue("oppoId",oppoId)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.OPPO_TEMPLE,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<TempleEntity>>() {})
        }
    }



    suspend fun addOpportunity(params:LinkedHashMap<String,Any?>):Any? {
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ADD_OPPORTUNITY,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }
    suspend fun updateOpportunity(params:LinkedHashMap<String,Any?>):Any? {
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.UPDATE_OPPORTUNITY,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }

    suspend fun deleteOpportunity(oppoId:String):Any?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue("reqId",oppoId)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.DELETE_OPPORTUNITY,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }

    suspend fun transferOpportunity(params:LinkedHashMap<String,Any?>):Any?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.TRANSFER_OPPORTUNITY,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }

    suspend fun dispatchOrder(params:LinkedHashMap<String,Any?>):Any?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.DISPATCH_ORDER,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }



}