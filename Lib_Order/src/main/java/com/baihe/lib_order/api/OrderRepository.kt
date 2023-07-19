package com.baihe.lib_order.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.api.CommonRepository
import com.baihe.lib_common.entity.OpportunityListItemEntity
import com.baihe.lib_common.entity.ResultEntity
import com.baihe.lib_common.entity.TempleEntity
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_common.provider.OpportunityServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_order.OrderDetailEntity
import com.baihe.lib_order.OrderListItemEntity
import com.baihe.lib_order.OrderTempleEntity
import com.baihe.lib_order.constant.UrlConstant
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip

class OrderRepository(lifecycle: LifecycleOwner): BaseRepository(lifecycle) {

    suspend fun orderList(page:Int,type:String?="0",name:String?,filter:LinkedHashMap<String,Any?>?,pageSize:Int = 10): ListData<OrderListItemEntity>? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("page",page)
                .putParamValue("name",name)
                .putParamValue("type",type)
                .putParamValue("create_time_order","1")
                .putParamValue("pageSize",pageSize)
                .putParamValue(filter)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_ORDER_LIST,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ListData<OrderListItemEntity>>>() {})
        }
    }

    suspend fun orderDetail(orderId:String):OrderDetailEntity?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("orderId",orderId)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_DETAIL,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<OrderDetailEntity>>() {})
        }
    }

    suspend fun addOrderWithOppo(reqId:String,customerId:String):ResultEntity? {
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue("reqId",reqId)
                .putParamValue("customerId",customerId)
                .putParamValue("ownerId",UserServiceProvider.getUserId())
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_CREATE_ORDER,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ResultEntity>>() {})
        }
    }

    @OptIn(FlowPreview::class)
    suspend fun addOrderWithoutOppo(params:LinkedHashMap<String,Any?>):ResultEntity? {
        var result:ResultEntity? = null
        flow {
            emit(OpportunityServiceProvider.addOpportunity(lifecycleOwner,params))
         }.flatMapConcat { value: ResultEntity? ->
                 flow {
                     value?.let {
                     emit(addOrderWithOppo(value.req_id!!,value.customerId!!))
                 }

             }
             }
             .collect{
                result = it
             }
        return result
    }

    suspend fun chargeBackOrder(params:LinkedHashMap<String,Any?>):Any?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_CHARGE_BACK,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }

    @OptIn(FlowPreview::class)
    suspend fun chargeBackOrderWithAttachment(params: LinkedHashMap<String, Any?>, attachmentList:List<String>):Any?{
        val repository = CommonRepository(lifecycleOwner)
        var result:Any? = null
        flow {
            emit(repository.batchUploadFiles(attachmentList))
        }.flatMapConcat {fileUrls->
            flow {
                val attachment = StringBuffer()
                fileUrls.forEach {
                    attachment.append(it)
                    if (fileUrls.indexOf(it)<fileUrls.size-1)
                        attachment.append(",")
                }
                params["file"] = attachment
                val submit = chargeBackOrder(params)
                emit(submit)
            }
        }.collect {
            result = it
        }
        return result
    }

    suspend fun transferOrder(params:LinkedHashMap<String,Any?>):Any?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_UP_ORDER_OWNER_ID,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }

    suspend fun addPeople(postData:List<KeyValueEntity>):Any?{
        return requestResponse {
            val params = mutableListOf<LinkedHashMap<String,Any>>()
            postData.forEach{
                params.add(linkedMapOf<String, Any>().apply {
                    put("name",it.name)
                    put("value",it.value)
                    put("defaultvalue",it.defaultValue)
                    put("order_id",it.order_id)
                })
            }

            val jsonParam = JsonParam.newInstance()
                .putParamValue("postData",params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_ADD_PEOPLE,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }


    suspend fun orderPeopleInfo(orderId:String):List<KeyValueEntity>?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue("orderId",orderId)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_PEOPLE_INFO,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<List<KeyValueEntity>>>() {})
        }
    }

    suspend fun followLog(params:LinkedHashMap<String,Any?>):Any?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_FOLLOW_LOG,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }

    suspend fun comeIndoor(params:LinkedHashMap<String,Any?>):Any?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_COME_INDOOR,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }

    suspend fun sign(params:LinkedHashMap<String,Any?>):Any?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ORDER_SIGN,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }

    suspend fun getCompanyConfig():ResultEntity?{
        return requestResponse {
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.CONTRACT_CONFIG,null))
                .execute(object :ResponseClass<BaseResponse<ResultEntity>>() {})
        }
    }









}