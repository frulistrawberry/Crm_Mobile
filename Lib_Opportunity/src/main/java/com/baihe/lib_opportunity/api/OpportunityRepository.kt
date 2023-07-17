package com.baihe.lib_opportunity.api

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.entity.CustomerDetailEntity
import com.baihe.lib_common.entity.OpportunityListItemEntity
import com.baihe.lib_common.entity.ResultEntity
import com.baihe.lib_common.entity.TempleEntity
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_common.provider.CustomerServiceProvider
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_opportunity.OpportunityDetailEntity
import com.baihe.lib_opportunity.constant.UrlConstant
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class OpportunityRepository(lifecycle: LifecycleOwner): BaseRepository(lifecycle) {
    suspend fun opportunityList(page:Int,isHistorical:String?="0",name:String?="",filter:LinkedHashMap<String,Any?>?=null,pageSize:Int = 10): ListData<OpportunityListItemEntity>? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("page",page)
                .putParamValue("name",name)
                .putParamValue("isHistorical",isHistorical)
                .putParamValue("create_time_order","1")
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

    suspend fun getOpportunityTemple(oppoId: String?=null, customerId:String?=null):TempleEntity?{
        var result:TempleEntity? = null
        val customerInfoFlow = flow {
            val customerInfo = CustomerServiceProvider.getCustomerInfo(customerId!!,lifecycleOwner!!)
            emit(customerInfo)
        }

        val templeFlow = flow {
            val temple =  requestResponse {
                val jsonParam = JsonParam.newInstance()
                if (!oppoId.isNullOrEmpty()){
                    jsonParam.putParamValue("reqId",oppoId)
                    jsonParam.putParamValue("customerId",customerId)
                }
                EasyHttp.get(lifecycleOwner)
                    .api(CommonApi(UrlConstant.OPPO_TEMPLE,jsonParam.getParamValue()))
                    .execute(object : ResponseClass<BaseResponse<TempleEntity>>() {})

            }
            emit(temple)
        }

        val resultFlow = customerInfoFlow.combine(templeFlow){
                customer: CustomerDetailEntity?, temple: TempleEntity? ->
            temple?.let {
                it.row.forEach { keyValueEntity ->
                    when(keyValueEntity.name){
                        "来源渠道"-> {
                            customer?.let {customerDetailEntity ->
                                keyValueEntity.is_channge = "1"
                                keyValueEntity.value = customerDetailEntity.sourceChannelId
                                keyValueEntity.defaultValue= customerDetailEntity.sourceChannel
                            }
                        }
                        "客户姓名" ->{
                            customer?.let {customerDetailEntity ->
                                keyValueEntity.is_channge = "1"
                                keyValueEntity.value = customerDetailEntity.name
                                keyValueEntity.defaultValue= customerDetailEntity.name
                            }
                        }
                        "联系方式" ->{
                            customer?.let {customerDetailEntity ->
                                keyValueEntity.is_channge = "1"
                                keyValueEntity.value = "${customerDetailEntity.see_phone?:""},${customerDetailEntity.wechat?:""}"
                                keyValueEntity.defaultValue= "${customerDetailEntity.phone?:""},${customerDetailEntity.wechat?:""}"
                            }
                        }
                        "提供人" ->{
                            customer?.let {customerDetailEntity ->
                                keyValueEntity.is_channge = "2"
                                keyValueEntity.channelId = customerDetailEntity.sourceChannelId
                                keyValueEntity.value = customerDetailEntity.recordUserId
                                keyValueEntity.defaultValue= customerDetailEntity.recordUser
                            }
                        }
                        "客户身份"->{
                            customer?.let {customerDetailEntity ->
                                keyValueEntity.is_channge = "2"
                                keyValueEntity.value = customerDetailEntity.identityId
                                keyValueEntity.defaultValue= customerDetailEntity.identity
                            }
                        }

                    }
                }
                temple
            }
        }

        if (oppoId.isNullOrEmpty() && !customerId.isNullOrEmpty()){
            //通过客户创建机会
            resultFlow.collect{
                result = it
            }
            return result

        }else{
            //新增/编辑机会
            return requestResponse {
                val jsonParam = JsonParam.newInstance()
                if (!oppoId.isNullOrEmpty()&&!customerId.isNullOrEmpty()){
                    jsonParam.putParamValue("reqId",oppoId)
                    jsonParam.putParamValue("customerId",customerId)
                }
                EasyHttp.get(lifecycleOwner)
                    .api(CommonApi(UrlConstant.OPPO_TEMPLE,jsonParam.getParamValue()))
                    .execute(object : ResponseClass<BaseResponse<TempleEntity>>() {})
            }
        }



    }



    suspend fun addOpportunity(params:LinkedHashMap<String,Any?>): ResultEntity? {
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ADD_OPPORTUNITY,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ResultEntity>>() {})
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