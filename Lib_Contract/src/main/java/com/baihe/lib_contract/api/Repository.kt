package com.baihe.lib_contract.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_contract.ContractDetailEntity
import com.baihe.lib_contract.ContractListItemEntity
import com.baihe.lib_contract.constant.UrlConstant

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
}