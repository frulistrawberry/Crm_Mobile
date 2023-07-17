package com.baihe.lib_common.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.constant.UrlConstant
import com.baihe.lib_common.entity.*
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.api.UploadApi
import com.baihe.lib_common.http.exception.ExceptionHandler
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.Data
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.helper.AppHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import java.nio.charset.Charset

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

    suspend fun getCompanyUser():List<RecordUserEntity>?{
        return requestResponse {
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.SEARCH_USER,null))
                .execute(object : ResponseClass<BaseResponse<List<RecordUserEntity>>>() {})
        }
    }

    suspend fun getUserChannelList():List<KeyValueEntity>?{
        return requestResponse {
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.GET_USER_CHANNEL_LIST,null))
                .execute(object : ResponseClass<BaseResponse<List<KeyValueEntity>>>() {})
        }
    }

    suspend fun call(customerId:String):String?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("id",customerId)
                .putParamValue("companyId",UserServiceProvider.getCompanyId())
                .putParamValue("type","customer")
                .putParamValue("status","1")
                .putParamValue("companyLineId","2")
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

    suspend fun getPhotoList():List<LocalPhotoEntity>?{
        return requestResponse {
            val albumParser = AlbumParser(AppHelper.getApplication())
            albumParser.parseAlbum()
            val photoInfos = albumParser.photoInfos
            val data  = Data(photoInfos,0,"3.5.0")
            BaseResponse(200,"请求成功",data)
        }

    }

    suspend fun addReqFollow(params:LinkedHashMap<String,Any?>):Any?{
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.FOLLOW_LOG,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Any>>() {})
        }
    }

    suspend fun getCityList():List<CityEntity>?{
        return requestResponse {
            var result:List<CityEntity>?
            try {
                val inputStream = AppHelper.getApplication().resources.assets.open("area.json")
                val byteArray = ByteArray(inputStream.available())
                inputStream.read(byteArray)
                val json = String(byteArray, Charset.forName("UTF-8"))
                result = Gson().fromJson(json,object :TypeToken<List<CityEntity>>(){}.type)
            }catch (e:Exception){
                throw ExceptionHandler.handleException(e)
            }
            val data  = Data(result,0,"3.5.0")
            BaseResponse(200,"请求成功",data)

        }
    }

//    suspend fun uploadFile(filePathList:List<String>):Any{
//        return requestResponse {
//            val result = mutableListOf<String>()
//            filePathList.asFlow().onEach {
//
//            }}
//    }



}