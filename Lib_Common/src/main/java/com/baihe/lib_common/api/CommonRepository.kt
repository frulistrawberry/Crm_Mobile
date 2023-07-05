package com.baihe.lib_common.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.constant.UrlConstant
import com.baihe.lib_common.entity.ChannelEntity
import com.baihe.lib_common.entity.FollowEntity
import com.baihe.lib_common.entity.RecordUserDataEntity
import com.baihe.lib_common.entity.RecordUserEntity
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_common.provider.UserServiceProvider

class CommonRepository(lifecycle: LifecycleOwner): BaseRepository(lifecycle) {

    suspend fun getRecordUser(channelId:String):RecordUserDataEntity?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("type",1)
                .putParamValue("channelId",channelId)
                .putParamValue("source","newCustomer")
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.GET_FLOW_INFO,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<RecordUserDataEntity>>() {})
        }
    }

    suspend fun getUserChannelList():List<ChannelEntity>?{
        return requestResponse {
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.GET_USER_CHANNEL_LIST,null))
                .execute(object : ResponseClass<BaseResponse<List<ChannelEntity>>>() {})
        }
    }

    suspend fun call(customerId:String):String?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("id",customerId)
                .putParamValue("companyId",UserServiceProvider.getCompanyId())
                .putParamValue("type","customer")
                .putParamValue("status","1")
                .putParamValue("platform","Mobile")
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.ALL_DIA_OUT,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<String>>() {})
        }
    }

    suspend fun getFollowDetail(followId:String):FollowEntity?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("followId",followId)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.FOLLOW_DETAIL,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<FollowEntity>>() {})
        }
    }



}