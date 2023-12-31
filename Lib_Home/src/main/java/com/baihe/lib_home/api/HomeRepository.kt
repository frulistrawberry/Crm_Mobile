package com.baihe.lib_home.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_framework.ext.TimeExt.formattedDate
import com.baihe.lib_home.DataEntity
import com.baihe.lib_home.HomeEntity
import com.baihe.lib_home.WaitingEntity
import com.baihe.lib_home.constant.UrlConstant
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class HomeRepository(lifecycleOwner: LifecycleOwner) : BaseRepository(lifecycleOwner) {
    suspend fun getWaitingList(page:Int,type:String = "1",pageSize:Int = 10):List<WaitingEntity>?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("page",page)
                .putParamValue("newType",type)
                .putParamValue("pageSize",pageSize)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.WAITING_LIST,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<List<WaitingEntity>>>() {})
        }
    }

    suspend fun getHomeData(startDate: String,endDate: String):HomeEntity?{
        var result:HomeEntity? = null
        val waitingListFlow = flow {
            val waitingList = getWaitingList(1, pageSize = 2)
            emit(waitingList)
        }
        val dataViewFlow = flow {
            val dataView = getDataView(startDate,endDate)
            emit(dataView)
        }
        val resultFlow = waitingListFlow.combine(dataViewFlow){
                first: List<WaitingEntity>?, second: DataEntity? ->
            HomeEntity(first,second)
        }
        resultFlow.collect {
            result = it
        }

        return result
    }

    suspend fun getDataView(startDate:String = System.currentTimeMillis().formattedDate(), endDate:String = System.currentTimeMillis().formattedDate()):DataEntity?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("timeBegin",startDate)
                .putParamValue("timeEnd",endDate)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.DATA_VIEW,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<DataEntity>>() {})
        }
    }


}