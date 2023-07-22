package com.baihe.lib_contract.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.api.CommonRepository
import com.baihe.lib_common.entity.ResultEntity
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_contract.ContractDetailEntity
import com.baihe.lib_contract.ContractListItemEntity
import com.baihe.lib_contract.TempleEntity
import com.baihe.lib_contract.constant.UrlConstant
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class Repository(lifecycleOwner: LifecycleOwner):BaseRepository(lifecycleOwner) {

    suspend fun contractList(page:Int,name:String?,pageSize:Int = 10): ListData<ContractListItemEntity>? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("page",page)
                .putParamValue("name",name)
                .putParamValue("pageSize",pageSize)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.CONTRACT_LIST,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ListData<ContractListItemEntity>>>() {})
        }
    }

    suspend fun contractDetail(contractId:String):ContractDetailEntity?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("contractId",contractId)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.CONTRACT_DETAIL,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ContractDetailEntity>>() {})
        }
    }

    suspend fun getContractTemple(orderId: String?=null,contractId: String?=null): TempleEntity?{
        return requestResponse {
                val jsonParam = JsonParam.newInstance()
                if (!contractId.isNullOrEmpty()){
                    jsonParam.putParamValue("contractId",contractId)
                }else if (!orderId.isNullOrEmpty()){
                    jsonParam.putParamValue("orderId",orderId)
                }
                EasyHttp.get(lifecycleOwner)
                    .api(CommonApi(UrlConstant.CONTRACT_TEMP,jsonParam.getParamValue()))
                    .execute(object : ResponseClass<BaseResponse<TempleEntity>>() {})
        }
    }

    suspend fun addContract(params:LinkedHashMap<String,Any?>):ResultEntity?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
            jsonParam.putParamValue(params)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.CREATE_CONTRACT,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ResultEntity>>() {})
        }
    }

    @OptIn(FlowPreview::class)
    suspend fun addContractWithAttachment(params:LinkedHashMap<String,Any?>, attachmentList:List<String>):ResultEntity?{
        val repository = CommonRepository(lifecycleOwner)
        var result:ResultEntity? = null
        flow {
            emit(repository.batchUploadFiles(attachmentList))
        }.flatMapConcat { fileUrls->
            flow {
                val attachment = StringBuffer()
                fileUrls.forEach {
                    attachment.append(it)
                    if (fileUrls.indexOf(it)<fileUrls.size-1)
                        attachment.append(",")
                }
                params["contract_pic"] = attachment
                val submit = addContract(params)
                emit(submit)
            }
        }.collect {
            result = it
        }
        return result
    }
    suspend fun updateContractWithAttachment(params: LinkedHashMap<String, Any?>,attachmentList: List<String>):ResultEntity?{
        val repository = CommonRepository(lifecycleOwner)
        var result:ResultEntity? = null
        val newAttachmentList = mutableListOf<String>()
        val oldAttachmentList:MutableList<String> = mutableListOf()
        attachmentList.forEach {
            if (!it.startsWith("http"))
                newAttachmentList.add(it)
            else
                oldAttachmentList.add(it)
        }
        flow {
            emit(repository.batchUploadFiles(newAttachmentList))
        }.flatMapConcat { fileUrls->
            flow {
                fileUrls.forEach {
                    oldAttachmentList.add(it!!)
                }
                val attachment = StringBuffer()
                oldAttachmentList.forEach {
                    attachment.append(it)
                    if (oldAttachmentList.indexOf(it)<oldAttachmentList.size-1)
                        attachment.append(",")
                }
                params["contract_pic"] = attachment
                val submit = updateContract(params)
                emit(submit)
            }
        }.collect {
            result = it
        }
        return result
    }

    suspend fun updateContract(params:LinkedHashMap<String,Any?>):ResultEntity?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
            jsonParam.putParamValue(params)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.UPDATE_CONTRACT,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ResultEntity>>() {})
        }
    }
}